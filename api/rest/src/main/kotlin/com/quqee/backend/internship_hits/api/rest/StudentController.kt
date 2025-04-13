package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.model.rest.CreateStudentView
import com.quqee.backend.internship_hits.model.rest.DeaneryEditStudentRequestView
import com.quqee.backend.internship_hits.model.rest.GetStudentInviteLinkRequestView
import com.quqee.backend.internship_hits.model.rest.GetStudentInviteLinkResponseView
import com.quqee.backend.internship_hits.model.rest.GetStudentsListResponseView
import com.quqee.backend.internship_hits.model.rest.StudentView
import com.quqee.backend.internship_hits.students.StudentsService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class StudentController(
    private val studentsService: StudentsService,
) : StudentsApiDelegate {
    override fun studentsDeaneryEditPost(deaneryEditStudentRequestView: DeaneryEditStudentRequestView): ResponseEntity<StudentView> {
        return super.studentsDeaneryEditPost(deaneryEditStudentRequestView)
    }

    override fun studentsListGet(
        lastId: UUID?,
        size: Int?,
        course: Int?,
        group: String?,
        orderBy: String?
    ): ResponseEntity<GetStudentsListResponseView> {
        return super.studentsListGet(lastId, size, course, group, orderBy)
    }

    override fun studentsRegistrationLinkPost(getStudentInviteLinkRequestView: GetStudentInviteLinkRequestView): ResponseEntity<GetStudentInviteLinkResponseView> {
        return super.studentsRegistrationLinkPost(getStudentInviteLinkRequestView)
    }

    override fun studentsRegistrationPost(createStudentView: CreateStudentView): ResponseEntity<StudentView> {
        return super.studentsRegistrationPost(createStudentView)
    }

    override fun studentsUserIdMoveAcademicPost(userId: UUID): ResponseEntity<StudentView> {
        return super.studentsUserIdMoveAcademicPost(userId)
    }

    override fun studentsUserIdRemoveAcademicPost(userId: UUID): ResponseEntity<StudentView> {
        return super.studentsUserIdRemoveAcademicPost(userId)
    }
}