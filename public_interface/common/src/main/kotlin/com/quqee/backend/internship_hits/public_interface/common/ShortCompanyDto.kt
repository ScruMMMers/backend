package com.quqee.backend.internship_hits.public_interface.common

import java.util.UUID

data class ShortCompanyDto(
    val companyId: UUID,
    val name: String,
    val avatarUrl: String,
    val primaryColor: String
)
