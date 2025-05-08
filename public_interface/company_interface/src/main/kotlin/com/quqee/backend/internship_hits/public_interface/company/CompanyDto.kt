package com.quqee.backend.internship_hits.public_interface.company

import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import com.quqee.backend.internship_hits.public_interface.company_position.CompanyPositionDto
import java.net.URI
import java.time.OffsetDateTime
import java.util.*

data class CompanyDto(
    val companyId: UUID,
    val name: String,
    val agent: ShortAccountDto?,
    val avatarUrl: URI,
    val sinceYear: String,
    val description: String,
    val primaryColor: ColorEnum,
    val positions: List<CompanyPositionDto>,
    val createdAt: OffsetDateTime
)