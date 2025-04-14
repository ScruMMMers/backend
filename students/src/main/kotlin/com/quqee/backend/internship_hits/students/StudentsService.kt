package com.quqee.backend.internship_hits.students

import com.quqee.backend.internship_hits.company.service.CompanyService
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationResponse
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.profile_public.GetProfileDto
import com.quqee.backend.internship_hits.public_interface.students_public.GetStudentsListDto
import com.quqee.backend.internship_hits.public_interface.students_public.StudentDto
import com.quqee.backend.internship_hits.students.entity.StudentEntity
import com.quqee.backend.internship_hits.students.repository.StudentsFilterParams
import com.quqee.backend.internship_hits.students.repository.StudentsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class StudentsService(
    private val profileService: ProfileService,
    private val studentsRepository: StudentsRepository,
    private val companyService: CompanyService,
) {
    fun getStudentsList(dto: GetStudentsListDto): LastIdPaginationResponse<StudentDto, UserId> {
        val students = studentsRepository.getStudents(
            pagination = dto.pagination,
            filter = StudentsFilterParams(
                course = dto.filter.course,
                group = dto.filter.group,
                logType = dto.filter.logType,
                logApprovalStatus = dto.filter.logApprovalStatus,
                positionType = dto.filter.positionType,
                positionName = dto.filter.positionName,
            )
        )
        val studentDtos = runBlocking {
            val deferred = students.map { studentEntity ->
                async {
                    mapStudentToDto(studentEntity)
                }
            }
            deferred.awaitAll()
        }

        return LastIdPaginationResponse(
            studentDtos,
            dto.pagination
        )
    }

    fun createUser() {

    }

    fun moveStudentToAcademic(userId: UserId): StudentDto {
        val studentEntity = studentsRepository.getStudent(userId)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Студент не найден")
        if (studentEntity.isOnAcademicLeave) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Студент уже находится в академе")
        }

        val updatedStudent = studentEntity.copy(isOnAcademicLeave = true)
        return mapStudentToDto(studentsRepository.updateStudent(updatedStudent))
    }

    fun removeStudentFromAcademic(userId: UserId): StudentDto {
        val studentEntity = studentsRepository.getStudent(userId)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Студент не найден")
        if (!studentEntity.isOnAcademicLeave) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Студент и так не в академе")
        }

        val updatedStudent = studentEntity.copy(isOnAcademicLeave = false)
        return mapStudentToDto(studentsRepository.updateStudent(updatedStudent))
    }

    private fun mapStudentToDto(entity: StudentEntity): StudentDto {
        val profile = profileService.getShortAccount(
            GetProfileDto(userId = entity.userId)
        )
        val company = entity.companyId?.let { companyService.getShortCompany(it) }

        return StudentDto(
            id = entity.userId,
            profile = profile,
            group = entity.group,
            course = entity.course,
            company = company,
        )
    }
}