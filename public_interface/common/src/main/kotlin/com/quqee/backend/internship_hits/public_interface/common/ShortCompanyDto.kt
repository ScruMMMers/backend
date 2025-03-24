package com.quqee.backend.internship_hits.public_interface.common

import java.net.URI
import java.util.UUID

data class ShortCompanyDto(
    val companyId: UUID,
    val name: String,
    val avatarUrl: URI,
    val primaryColor: String
)
