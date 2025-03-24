package com.quqee.backend.internship_hits.public_interface.company

import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import java.util.UUID

data class CreateCompanyDto (
    val name: String,
    val sinceYear: String,
    val agentId: UUID,
    val description: String,
    val primaryColor: ColorEnum
)