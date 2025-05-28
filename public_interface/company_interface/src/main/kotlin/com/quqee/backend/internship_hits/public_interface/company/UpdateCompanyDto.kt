package com.quqee.backend.internship_hits.public_interface.company

import java.util.UUID

data class UpdateCompanyDto (
    val name: String?,
    val sinceYear: String?,
    val avatarId: UUID?,
    val agentId: UUID?,
    val description: String?,
    val primaryColor: String?
)