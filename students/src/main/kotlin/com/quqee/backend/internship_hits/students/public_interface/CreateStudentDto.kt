package com.quqee.backend.internship_hits.students.public_interface

import com.quqee.backend.internship_hits.public_interface.common.UserId
import java.util.UUID

data class CreateStudentDto(
    val userId: UserId,
    val course: Int,
    val group: String,
    val isOnAcademicLeave: Boolean,
    val companyId: UUID?,
)
