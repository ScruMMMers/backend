package com.quqee.backend.internship_hits.public_interface.tags

import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import java.util.UUID

data class TagDto(
    val id: UUID,
    val shortCompany: ShortCompanyDto,
    val name: String
)
