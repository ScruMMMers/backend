package com.quqee.backend.internship_hits.public_interface.company

import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import java.util.UUID

data class UpdateCompanyDto (
    val name: String?,
    val sinceYear: String?,
    val avatarId: UUID?,
    val agentId: UUID?,
    val description: String?,
    val primaryColor: ColorEnum?
)