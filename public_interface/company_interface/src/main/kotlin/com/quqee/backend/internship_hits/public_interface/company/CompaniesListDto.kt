package com.quqee.backend.internship_hits.public_interface.company

import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto

data class CompaniesListDto (
    val companies: List<ShortCompanyDto>,
    val page: LastIdPagination
)