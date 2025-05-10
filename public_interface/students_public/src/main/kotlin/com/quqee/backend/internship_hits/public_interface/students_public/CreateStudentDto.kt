package com.quqee.backend.internship_hits.public_interface.students_public

import java.util.UUID

data class CreateStudentDto(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val course: Int,
    val group: String,
    val middleName: String?,
    val photoId: String?,
    val inviteLinkId: UUID,
)
