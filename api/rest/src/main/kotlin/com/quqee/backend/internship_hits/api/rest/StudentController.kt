package com.quqee.backend.internship_hits.api.rest

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.quqee.backend.internship_hits.mapper.EnumerationMapper
import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationResponse
import com.quqee.backend.internship_hits.public_interface.common.enums.Position
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.students_public.*
import com.quqee.backend.internship_hits.students.StudentsService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class StudentController(
    private val studentsService: StudentsService,
    private val logTypeMapper: EnumerationMapper<LogTypeEnum, LogType>,
    private val approvalStatusMapper: EnumerationMapper<ApprovalStatusEnum, ApprovalStatus>,
    private val positionNameMapper: EnumerationMapper<PositionEnum, Position>,
    private val lastIdPaginationResponseMapper: FromInternalToApiMapper<LastIdPaginationView, LastIdPaginationResponse<*, UUID>>,
    private val studentViewMapper: FromInternalToApiMapper<StudentView, StudentDto>,
    private val moveByCourseMapper: FromApiToInternalMapper<MoveStudentsViewByCourse, MoveToCourseDto>,
    private val moveByUserMapper: FromApiToInternalMapper<MoveStudentsViewByUser, MoveToCourseDto>,
    private val objectMapper: ObjectMapper,
) : StudentsApiDelegate {
    override fun studentsDeaneryEditPost(deaneryEditStudentRequestView: DeaneryEditStudentRequestView): ResponseEntity<StudentView> {
        val dto = DeaneryEditStudentDto(
            studentId = deaneryEditStudentRequestView.studentId,
            fullName = deaneryEditStudentRequestView.fullName,
            group = deaneryEditStudentRequestView.group,
            course = deaneryEditStudentRequestView.course,
            companyId = deaneryEditStudentRequestView.companyId,
        )
        val student = studentsService.deaneryEditStudent(dto)
        return ResponseEntity.ok(studentViewMapper.fromInternal(student))
    }

    override fun studentsRegistrationLinkPost(getStudentInviteLinkRequestView: GetStudentInviteLinkRequestView): ResponseEntity<GetStudentInviteLinkResponseView> {
        val dto = CreateInviteLinkDto(
            course = getStudentInviteLinkRequestView.course,
            group = getStudentInviteLinkRequestView.group,
        )
        val result = studentsService.createInviteLink(dto)
        return ResponseEntity.ok(
            GetStudentInviteLinkResponseView(
                link = result.link,
            )
        )
    }

    override fun studentsRegistrationPost(createStudentView: CreateStudentView): ResponseEntity<StudentView> {
        val dto = CreateStudentDto(
            username = createStudentView.username,
            email = createStudentView.email,
            firstName = createStudentView.firstName,
            lastName = createStudentView.lastName,
            password = createStudentView.password,
            course = createStudentView.course,
            group = createStudentView.group,
            middleName = createStudentView.middleName,
            photoId = createStudentView.photoId,
            inviteLinkId = createStudentView.inviteLinkId,
        )
        val student = studentsService.createStudent(dto)
        return ResponseEntity.ok(studentViewMapper.fromInternal(student))
    }

    override fun studentsListGet(
        lastId: UUID?,
        size: Int?,
        course: List<Int>?,
        group: List<String>?,
        logType: List<LogTypeEnum>?,
        logApprovalStatus: List<ApprovalStatusEnum>?,
        positionType: List<PositionEnum>?,
        positionName: List<String>?,
        orderBy: String?,
        companyId: List<UUID>?,
        logByCompany: String?,
        name: String?,
    ): ResponseEntity<GetStudentsListResponseView> {
        val dto = GetStudentsListDto(
            pagination = LastIdPaginationRequest(
                lastId = lastId,
                pageSize = size,
            ),
            filter = GetStudentsListFilterParamDto(
                course = course?.toSet(),
                group = group?.toSet(),
                logType = logType?.map { logTypeMapper.mapToInternal(it) }?.toSet(),
                logApprovalStatus = logApprovalStatus?.map { approvalStatusMapper.mapToInternal(it) }?.toSet(),
                positionType = positionType?.map { positionNameMapper.mapToInternal(it) }?.toSet(),
                positionName = positionName?.toSet(),
                companyIds = companyId?.toSet(),
                logByCompany = logByCompany?.let {
                    objectMapper.readValue(it, object : TypeReference<Map<LogType, LogByCompanyItem>>() {})
                },
                name = name?.takeIf { it.isNotBlank() }?.trim(),
            ),
        )
        val students = studentsService.getStudentsList(dto)
        return ResponseEntity.ok(
            GetStudentsListResponseView(
                students = students.responseCollection.map { studentViewMapper.fromInternal(it) },
                page = lastIdPaginationResponseMapper.fromInternal(students)
            )
        )
    }

    override fun studentsUserIdMoveAcademicPost(userId: UUID): ResponseEntity<StudentView> {
        studentsService.moveStudentToAcademic(userId)
        return ResponseEntity.ok().build()
    }

    override fun studentsUserIdRemoveAcademicPost(userId: UUID): ResponseEntity<StudentView> {
        studentsService.removeStudentFromAcademic(userId)
        return ResponseEntity.ok().build()
    }

    override fun studentsDeaneryMoveCoursePost(): ResponseEntity<Unit> {
        studentsService.moveToCourse(MoveToCourseAllDto())
        return ResponseEntity.ok().build()
    }

    override fun studentsDeaneryMoveCourseByCoursePost(moveStudentsViewByCourse: MoveStudentsViewByCourse): ResponseEntity<Unit> {
        val dto = moveByCourseMapper.fromApi(moveStudentsViewByCourse)
        studentsService.moveToCourse(dto)

        return ResponseEntity.ok().build()
    }

    override fun studentsDeaneryMoveCourseByUserPost(moveStudentsViewByUser: MoveStudentsViewByUser): ResponseEntity<Unit> {
        val dto = moveByUserMapper.fromApi(moveStudentsViewByUser)
        studentsService.moveToCourse(dto)

        return ResponseEntity.ok().build()
    }

    override fun studentsDeaneryDeleteUserIdDelete(userId: UUID): ResponseEntity<Unit> {
        studentsService.deaneryDeleteStudent(userId)
        return ResponseEntity.ok().build()
    }

    override fun studentsImportPost(postStudentsImportView: PostStudentsImportView): ResponseEntity<Unit> {
        studentsService.importStudents(postStudentsImportView.fileId)
        return ResponseEntity.ok().build()
    }
}