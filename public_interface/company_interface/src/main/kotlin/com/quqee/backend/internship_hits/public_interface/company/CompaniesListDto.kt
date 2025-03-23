package com.quqee.backend.internship_hits.public_interface.company

import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination

data class CompaniesListDto (
    val companies: List<CompanyDto>,
    val page: LastIdPagination
)