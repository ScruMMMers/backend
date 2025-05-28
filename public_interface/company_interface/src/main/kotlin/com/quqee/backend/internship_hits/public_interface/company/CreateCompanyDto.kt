package com.quqee.backend.internship_hits.public_interface.company

import java.util.*

data class CreateCompanyDto (
    val name: String,
    val sinceYear: String,
    val avatarId: UUID,
    val agentId: UUID?,
    val description: String,
    val primaryColor: String
)