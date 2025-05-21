package com.quqee.backend.internship_hits.marks.service

import com.quqee.backend.internship_hits.logs.repository.LogsRepository
import com.quqee.backend.internship_hits.logs.repository.jpa.LogsJpaRepository
import com.quqee.backend.internship_hits.marks.entity.MarkEntity
import com.quqee.backend.internship_hits.marks.mapper.MarkMapper
import com.quqee.backend.internship_hits.marks.repository.MarkRepository
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.public_interface.common.enums.DiaryStatusEnum
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.mark.CreateMarkDto
import com.quqee.backend.internship_hits.public_interface.mark.MarkDto
import com.quqee.backend.internship_hits.public_interface.mark.MarkListDto
import com.quqee.backend.internship_hits.students.StudentsService
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

interface MarkService {

    fun saveMark(userId: UserId, createMarkDto: CreateMarkDto): MarkDto

    fun createMarks(userId: UserId): List<MarkEntity>

    fun getMyCurrentMark(): MarkDto

    fun getDiaries(userId: UserId, course: Int): List<DiaryStatusEnum>

    fun getMyMarks(): MarkListDto

}

@Service
class MarkServiceImpl(
    private val markMapper: MarkMapper,
    private val studentsService: StudentsService,
    private val repository: MarkRepository,
    private val logsRepository: LogsJpaRepository,
) : MarkService {

    override fun saveMark(userId: UserId, createMarkDto: CreateMarkDto): MarkDto {
        val student = studentsService.getStudent(userId)

        if (createMarkDto.semester == null && student.course < 3) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Нельзя поставить оценку студенту с курсом ${student.course}")
        }

        if (createMarkDto.semester != null && createMarkDto.semester!! < 5) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Нельзя поставить оценку за ${createMarkDto.semester} семестр")
        }

        val semester = createMarkDto.semester ?: getCurrentSemester(student.course)

        val existingMark = repository.findByUserIdAndSemester(userId, semester).orElse(null)

        if (existingMark != null) {
            existingMark.mark = createMarkDto.mark
            existingMark.date = OffsetDateTime.now()
            repository.save(existingMark)

            return markMapper.mapToDto(existingMark, DiaryStatusEnum.NONE)
        } else {
            val marks = createMarks(userId)
            val diaries = getDiaries(userId, student.course)

            val mark = marks[semester - 5]
            val editedMark = mark.copy(mark = createMarkDto.mark)

            repository.save(mark)
            return markMapper.mapToDto(editedMark, diaries[semester - 5])
        }
    }

    override fun getMyCurrentMark(): MarkDto {
        val myId = getCurrentUser()
        val student = studentsService.getStudent(myId)

        val currentSemester = getCurrentSemester(student.course)

        val mark = repository.findByUserIdAndSemester(myId, currentSemester)
            .orElseGet {
                val newMark = MarkEntity(
                    id = UUID.randomUUID(),
                    userId = myId,
                    mark = null,
                    date = null,
                    semester = currentSemester
                )
                repository.save(newMark)
                newMark
            }

        val diaryStatus = getDiaries(myId, student.course)
            .let { diaries ->
                val index = currentSemester - 5
                if (index in diaries.indices) diaries[index] else DiaryStatusEnum.NONE
            }

        return markMapper.mapToDto(mark, diaryStatus)
    }

    override fun createMarks(userId: UserId): List<MarkEntity> {
        val marks = mutableListOf<MarkEntity>()

        for (semester in 5..8) {
            val mark = MarkEntity(
                id = UUID.randomUUID(),
                userId = userId,
                mark = null,
                date = null,
                semester = semester
            )
            marks.add(mark)
        }

        repository.saveAll(marks)

        return marks
    }

    override fun getDiaries(userId: UserId, course: Int): List<DiaryStatusEnum> {
        val diaryLogs = logsRepository.findAllByUserIdAndTypeOrderByCreatedAtDesc(
            userId = userId,
            type = LogType.PRACTICE_DIARY
        )

        val semesterStatusMap = mutableMapOf<Int, DiaryStatusEnum>()

        diaryLogs.forEach { log ->
            val semester = getSemesterByLogDate(course, log.createdAt)

            if (semester in 5..8 && !semesterStatusMap.containsKey(semester)) {
                semesterStatusMap[semester] = when (log.approvalStatus) {
                    ApprovalStatus.APPROVED -> DiaryStatusEnum.APPROVED
                    ApprovalStatus.REJECTED -> DiaryStatusEnum.REJECTED
                    ApprovalStatus.PENDING -> DiaryStatusEnum.PENDING
                }
            }
        }

        return (5..8).map { semester ->
            semesterStatusMap[semester] ?: DiaryStatusEnum.NONE
        }
    }

    override fun getMyMarks(): MarkListDto {
        val myId = getCurrentUser()
        val student = studentsService.getStudent(myId)

        var marks = repository.findAllByUserIdOrderBySemesterAsc(myId)
        if (marks.isEmpty()) {
            marks = createMarks(myId)
        }
        val diaries = getDiaries(myId, student.course)

        return zipMarks(diaries, marks)
    }

    private fun zipMarks(diaries: List<DiaryStatusEnum>, marks: List<MarkEntity>): MarkListDto {
        val myMarks = marks.zip(diaries) { mark, diaryStatus ->
            MarkDto(
                id = mark.id,
                userId = mark.userId,
                mark = mark.mark,
                diary = diaryStatus,
                date = mark.date,
                semester = mark.semester
            )
        }

        return markMapper.mapToListDto(myMarks)
    }

    private fun getCurrentSemester(course: Int): Int {
        val currentDate = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply { timeInMillis = currentDate }

        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val isFirstSemester = when (month) {
            in 9..12 -> true
            1 -> true
            2 -> day <= 15
            else -> false
        }

        return if (isFirstSemester) {
            course * 2 - 1
        } else {
            course * 2
        }
    }

    private fun getSemesterByLogDate(currentCourse: Int, logDate: OffsetDateTime): Int {
        val currentDate = OffsetDateTime.now()
        val logYear = logDate.year
        val currentYear = currentDate.year

        val yearDifference = currentYear - logYear

        val maxSemesterForCourse = currentCourse * 2

        val calculatedSemester = maxSemesterForCourse - (yearDifference * 2)

        val month = logDate.monthValue
        val day = logDate.dayOfMonth

        val isFirstSemester = when (month) {
            in 9..12 -> true
            1 -> true
            2 -> day <= 15
            else -> false
        }

        val semesterAdjustment = if (isFirstSemester) 1 else 0

        val finalSemester = (calculatedSemester + semesterAdjustment).coerceIn(1, 8)

        return finalSemester
    }

    private fun getCurrentUser(): UUID {
        return KeycloakUtils.getUserId() ?: throw ExceptionInApplication(ExceptionType.FORBIDDEN)
    }

}