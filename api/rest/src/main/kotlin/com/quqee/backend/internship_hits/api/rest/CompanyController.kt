package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.company.service.CompanyService
import org.springframework.stereotype.Component

@Component
class CompanyController(
    private val companyService: CompanyService
) : CompanyApiDelegate {
}