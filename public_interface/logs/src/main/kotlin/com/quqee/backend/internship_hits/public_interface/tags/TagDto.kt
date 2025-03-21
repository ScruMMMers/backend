package com.quqee.backend.internship_hits.public_interface.tags

import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto

data class TagDto(
    val id: String,
    val shortCompany: ShortCompanyDto,
    val name: String
)
