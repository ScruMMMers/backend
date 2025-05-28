package com.quqee.backend.internship_hits.marks.service

import com.quqee.backend.internship_hits.marks.entity.MarkEntity
import com.quqee.backend.internship_hits.marks.mapper.MarkMapper
import com.quqee.backend.internship_hits.marks.repository.MarkRepository
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.*
import com.quqee.backend.internship_hits.public_interface.common.enums.DiaryStatusEnum
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.mark.CreateMarkDto
import com.quqee.backend.internship_hits.public_interface.mark.CreateMarksListDto
import com.quqee.backend.internship_hits.students.StudentsService
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*
import kotlinx.coroutines.*
import org.springframework.transaction.annotation.Transactional
import javax.swing.SortOrder

interface MarkService {

    fun saveMarks(userId: UserId, createMarksListDto: CreateMarksListDto): MarkListDto

    fun createMarks(userId: UserId): List<MarkEntity>

    fun getMyCurrentMark(): MarkDto

    fun getMyMarks(): MarkListDto

    fun updateDiaryStatus(status: DiaryStatusEnum, userId: UserId, logDate: OffsetDateTime)

    fun getStudentsMarksList(
        search: String?,
        semester: Int?,
        diaryDoneFirst: Boolean?,
        diaryStatus: DiaryStatusEnum?,
        mark: Int?,
        orderByGroup: SortOrder?,
        lastId: UUID?,
        size: Int?
    ): StudentsMarksListDto

}

@Service
open class MarkServiceImpl(
    private val markMapper: MarkMapper,
    private val studentsService: StudentsService,
    private val repository: MarkRepository,
    private val profileService: ProfileService,
) : MarkService {

    @Transactional
    override fun saveMarks(userId: UserId, createMarksListDto: CreateMarksListDto): MarkListDto {
        return MarkListDto(
            createMarksListDto.marks.stream().map {
                saveMark(userId, it)
            }.toList()
        )
    }

    private fun saveMark(userId: UserId, createMarkDto: CreateMarkDto): MarkDto {
        val student = studentsService.getStudent(userId)

        if (createMarkDto.semester == null && student.course < 3) {
            throw ExceptionInApplication(
                ExceptionType.BAD_REQUEST,
                "Нельзя поставить оценку студенту с курсом ${student.course}"
            )
        }

        if (createMarkDto.semester != null && createMarkDto.semester!! < 5) {
            throw ExceptionInApplication(
                ExceptionType.BAD_REQUEST,
                "Нельзя поставить оценку за ${createMarkDto.semester} семестр"
            )
        }

        val semester = createMarkDto.semester ?: getSemester(student.course, OffsetDateTime.now())

        val existingMark = repository.findByUserIdAndSemester(userId, semester).orElse(null)

        if (existingMark != null) {
            existingMark.mark = createMarkDto.mark
            existingMark.date = OffsetDateTime.now()
            repository.save(existingMark)

            return markMapper.mapToDto(existingMark)
        } else {
            val marks = createMarks(userId)

            val mark = marks[semester - 5]
            val editedMark = mark.copy(mark = createMarkDto.mark)

            repository.save(mark)
            return markMapper.mapToDto(editedMark)
        }
    }

    override fun getMyCurrentMark(): MarkDto {
        val myId = getCurrentUser()
        val student = studentsService.getStudent(myId)

        val currentSemester = getSemester(student.course, OffsetDateTime.now())

        val mark = repository.findByUserIdAndSemester(myId, currentSemester)
            .orElseGet {
                val newMark = MarkEntity(
                    id = UUID.randomUUID(),
                    userId = myId,
                    mark = null,
                    diaryStatusEnum = DiaryStatusEnum.NONE,
                    date = null,
                    semester = currentSemester
                )
                repository.save(newMark)
                newMark
            }

        return markMapper.mapToDto(mark)
    }

    override fun createMarks(userId: UserId): List<MarkEntity> {
        val marks = mutableListOf<MarkEntity>()

        for (semester in 5..8) {
            val mark = MarkEntity(
                id = UUID.randomUUID(),
                userId = userId,
                mark = null,
                diaryStatusEnum = DiaryStatusEnum.NONE,
                date = null,
                semester = semester
            )
            marks.add(mark)
        }

        repository.saveAll(marks)

        return marks
    }

    override fun getMyMarks(): MarkListDto {
        val myId = getCurrentUser()

        var marks = repository.findAllByUserIdOrderBySemesterAsc(myId)
        if (marks.isEmpty()) {
            marks = createMarks(myId)
        }

        return MarkListDto(
            marks.map { markMapper.mapToDto(it) }
        )
    }

    override fun updateDiaryStatus(status: DiaryStatusEnum, userId: UserId, logDate: OffsetDateTime) {
        val course = studentsService.getStudent(userId).course
        val semester = getSemester(course, logDate)

        val existingMark = repository.findByUserIdAndSemester(userId, semester).orElse(null)

        if (existingMark != null) {
            val newMark = existingMark.copy(diaryStatusEnum = status)
            repository.save(newMark)
        } else {
            val marks = createMarks(userId)

            val mark = marks[semester - 5]
            val editedMark = mark.copy(diaryStatusEnum = status)

            repository.save(editedMark)
        }

    }

    override fun getStudentsMarksList(
        search: String?,
        semester: Int?,
        diaryDoneFirst: Boolean?,
        diaryStatus: DiaryStatusEnum?,
        mark: Int?,
        orderByGroup: SortOrder?,
        lastId: UUID?,
        size: Int?
    ): StudentsMarksListDto = runBlocking {
        val userIds = search?.let { profileService.getUserIdsByName(it) }

        val projections = repository.getStudentsWithMarks(
            userIds?.toList() ?: emptyList(),
            semester,
            diaryDoneFirst,
            diaryStatus?.toString(),
            mark,
            orderByGroup?.toString()
        )

        val filteredProjections = lastId?.let {
            projections.dropWhile { it.id != lastId }.drop(1)
        } ?: projections

        val takenProjections = filteredProjections.take(size ?: 10)

        val dtos = withContext(Dispatchers.Default) {
            takenProjections.map { projection ->
                async {
                    val fullName = try {
                        profileService.getShortAccount(GetProfileDto(userId = projection.id)).fullName
                    } catch (e: Exception) {
                        "Unknown"
                    }
                    projection.toStudentsMarksListDto(fullName)
                }
            }.awaitAll()
        }

        StudentsMarksListDto(
            dtos,
            LastIdPagination(
                lastId,
                size ?: 10,
                dtos.size < filteredProjections.size
            )
        )
    }

    private fun StudentsMarksProjection.toStudentsMarksListDto(
        userFullName: String,
        markDate: OffsetDateTime? = null
    ): StudentsMarksDto {
        return StudentsMarksDto(
            id = this.id,
            fullName = userFullName,
            group = this.group,
            course = this.course,

            marks = listOfNotNull(
                MarkDto(
                    id = UUID.randomUUID(),
                    userId = this.id,
                    mark = this.fifthSemesterMark,
                    diary = this.fifthSemesterDiary,
                    date = markDate,
                    semester = 5
                ),
                MarkDto(
                    id = UUID.randomUUID(),
                    userId = this.id,
                    mark = this.sixthSemesterMark,
                    diary = this.sixthSemesterDiary,
                    date = markDate,
                    semester = 6
                ),
                MarkDto(
                    id = UUID.randomUUID(),
                    userId = this.id,
                    mark = this.seventhSemesterMark,
                    diary = this.seventhSemesterDiary,
                    date = markDate,
                    semester = 7
                ),
                MarkDto(
                    id = UUID.randomUUID(),
                    userId = this.id,
                    mark = this.eighthSemesterMark,
                    diary = this.eighthSemesterDiary,
                    date = markDate,
                    semester = 8
                )
            )
        )
    }

    private fun getSemester(currentCourse: Int, date: OffsetDateTime): Int {
        val currentDate = OffsetDateTime.now()
        val logYear = date.year
        val currentYear = currentDate.year

        val yearDifference = currentYear - logYear

        val maxSemesterForCourse = currentCourse * 2

        val calculatedSemester = maxSemesterForCourse - (yearDifference * 2)

        val month = date.monthValue
        val day = date.dayOfMonth

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