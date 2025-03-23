package com.quqee.backend.internship_hits.public_interface.company

import java.util.UUID

data class CreateCompanyDto (
    val name: String,
    val sinceYear: String,
    val agentId: UUID,
    val description: String,
    val primaryColor: String
)