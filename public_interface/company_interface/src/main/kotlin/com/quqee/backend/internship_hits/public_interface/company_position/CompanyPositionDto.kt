package com.quqee.backend.internship_hits.public_interface.company_position

import java.util.*

data class CompanyPositionDto(
    val positionId: UUID,
    val name: String,
    val employedCount: Int,
    val interviewsCount: Int
)