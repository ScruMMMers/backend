package com.quqee.backend.internship_hits.marks.service

import com.quqee.backend.internship_hits.marks.entity.MarkEntity
import com.quqee.backend.internship_hits.marks.mapper.MarkMapper
import com.quqee.backend.internship_hits.marks.repository.MarkRepository
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.mark.CreateMarkDto
import com.quqee.backend.internship_hits.public_interface.mark.MarkDto
import com.quqee.backend.internship_hits.students.StudentsService
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

interface MarkService {

    fun createMark(createMarkDto: CreateMarkDto): MarkDto

    fun getMyMarks(): List<MarkDto>

    fun getMyCurrentMark(): MarkDto

}

@Service
class MarkServiceImpl(
    private val markMapper: MarkMapper,
    private val studentsService: StudentsService,
    private val repository: MarkRepository,
) : MarkService {
    override fun createMark(createMarkDto: CreateMarkDto): MarkDto {
        val student = studentsService.getStudent(createMarkDto.userId)

        val semester = createMarkDto.semester ?: getCurrentSemester(student.course)

        val existingMark = repository.findByUserIdAndSemester(createMarkDto.userId, semester).orElse(null)

        if (existingMark != null) {
            existingMark.mark = createMarkDto.mark
            existingMark.date = OffsetDateTime.now()
            repository.save(existingMark)

            return markMapper.mapToDto(existingMark)
        } else {
            val mark = MarkEntity(
                UUID.randomUUID(),
                createMarkDto.userId,
                createMarkDto.mark,
                OffsetDateTime.now(),
                semester
            )

            repository.save(mark)
            return markMapper.mapToDto(mark)
        }
    }

    override fun getMyMarks(): List<MarkDto> {
        val myId = getCurrentUser()

        return repository.findAllByUserIdOrderBySemesterDesc(myId).map { markMapper.mapToDto(it) }
    }

    override fun getMyCurrentMark(): MarkDto {
        val myId = getCurrentUser()

        return markMapper.mapToDto(
            repository.findFirstByUserIdOrderBySemesterDesc(myId)
                .orElseThrow { ExceptionInApplication(ExceptionType.NOT_FOUND) })
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

    private fun getCurrentUser(): UUID {
        return KeycloakUtils.getUserId() ?: throw ExceptionInApplication(ExceptionType.FORBIDDEN)
    }

}