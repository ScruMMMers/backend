package com.quqee.backend.internship_hits.public_interface.company_position

import java.util.*

data class CreateCompanyPositionDto(
    val name: String,
    val companyId: UUID
)