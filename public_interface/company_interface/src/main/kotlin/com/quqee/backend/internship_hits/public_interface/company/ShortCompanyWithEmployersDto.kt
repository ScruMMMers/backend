package com.quqee.backend.internship_hits.public_interface.company

import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import java.net.URI
import java.util.*

data class ShortCompanyWithEmployersDto(
    val companyId: UUID,
    val name: String,
    val avatarUrl: URI,
    val agent: ShortAccountDto?,
    val primaryColor: String,
    val sinceYear: String,
    val description: String,
    val employedCount: Int,
)
