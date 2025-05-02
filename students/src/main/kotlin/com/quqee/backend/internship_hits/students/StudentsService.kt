package com.quqee.backend.internship_hits.students

import com.quqee.backend.internship_hits.company.service.CompanyService
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.profile.dto.CreateUserDto
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationResponse
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.logs.ShortLogInfo
import com.quqee.backend.internship_hits.public_interface.profile_public.GetProfileDto
import com.quqee.backend.internship_hits.public_interface.students_public.GetStudentsListDto
import com.quqee.backend.internship_hits.public_interface.students_public.StudentDto
import com.quqee.backend.internship_hits.students.entity.StudentEntity
import com.quqee.backend.internship_hits.students.public_interface.CreateStudentDto
import com.quqee.backend.internship_hits.students.repository.StudentsFilterParams
import com.quqee.backend.internship_hits.students.repository.StudentsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.quqee.backend.internship_hits.public_interface.students_public.CreateStudentDto as ExternalCreateStudentDto

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

    @Transactional
    fun createStudent(dto: ExternalCreateStudentDto): StudentDto {
        val userId = profileService.createProfile(
            CreateUserDto(
                username = dto.username,
                email = dto.email,
                firstName = dto.firstName,
                lastName = dto.lastName,
                password = dto.password,
                roles = getRoleByCourse(dto.course),
                middleName = dto.middleName,
                photoId = dto.photoId,
            )
        )
        val student = studentsRepository.createStudent(
            CreateStudentDto(
                userId = userId,
                course = dto.course,
                group = dto.group,
                isOnAcademicLeave = false,
                companyId = null,
            )
        )
        return mapStudentToDto(student)
    }

    @Transactional
    fun moveStudentToAcademic(userId: UserId): StudentDto {
        val studentEntity = studentsRepository.getStudent(userId)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Студент не найден")
        if (studentEntity.isOnAcademicLeave) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Студент уже находится в академе")
        }

        val updatedStudent = studentEntity.copy(isOnAcademicLeave = true)
        return mapStudentToDto(studentsRepository.updateStudent(updatedStudent))
    }

    @Transactional
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
            logs = entity.logs.map { log ->
                ShortLogInfo(
                    id = log.id,
                    status = ApprovalStatus.valueOf(log.status),
                    type = LogType.valueOf(log.type),
                    createdAt = log.createdAt,
                )
            }.toSet()
        )
    }

    private fun getRoleByCourse(course: Int): Set<UserRole> {
        return when (course) {
            2 -> setOf(UserRole.STUDENT_SECOND)
            3 -> setOf(UserRole.STUDENT_THIRD)
            4 -> setOf(UserRole.STUDENT_FOURTH)
            else -> emptySet()
        }
    }
}