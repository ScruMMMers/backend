package com.quqee.backend.internship_hits.public_interface.company_position

import com.quqee.backend.internship_hits.public_interface.common.PositionDto
import java.util.*

data class CompanyPositionDto(
    val id: UUID,
    val companyId: UUID,
    val position: PositionDto,
    val employedCount: Int,
    val interviewsCount: Int
)