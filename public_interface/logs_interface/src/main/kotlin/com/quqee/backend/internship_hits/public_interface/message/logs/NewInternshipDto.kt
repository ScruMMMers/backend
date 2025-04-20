package com.quqee.backend.internship_hits.public_interface.message.logs

import java.util.UUID

data class NewInternshipDto(
    val userId: UUID,
    val companyId: UUID,
    val positionId: Long
)
