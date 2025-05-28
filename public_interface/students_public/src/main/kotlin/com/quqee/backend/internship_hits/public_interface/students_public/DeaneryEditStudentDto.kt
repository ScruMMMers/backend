package com.quqee.backend.internship_hits.public_interface.students_public

import com.quqee.backend.internship_hits.public_interface.common.UserId
import java.util.UUID

data class DeaneryEditStudentDto(
    val studentId: UserId,
    val fullName: String,
    val group: String,
    val course: Int,
    val companyId: UUID?,
)
