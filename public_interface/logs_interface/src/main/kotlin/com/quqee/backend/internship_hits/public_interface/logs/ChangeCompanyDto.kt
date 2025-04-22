package com.quqee.backend.internship_hits.public_interface.logs

import java.util.*

data class ChangeCompanyDto(
    val companyId: UUID,
    val positionId: Long
)