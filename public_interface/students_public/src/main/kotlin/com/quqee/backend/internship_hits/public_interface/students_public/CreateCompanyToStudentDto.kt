package com.quqee.backend.internship_hits.public_interface.students_public

import com.quqee.backend.internship_hits.public_interface.common.UserId
import java.util.UUID

data class CreateCompanyToStudentDto(
    val companyId: UUID,
    val positionId: Long?,
    val userId: UserId
)
