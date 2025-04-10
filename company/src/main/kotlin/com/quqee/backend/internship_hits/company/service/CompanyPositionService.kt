package com.quqee.backend.internship_hits.company.service

import com.quqee.backend.internship_hits.company.repository.CompanyPositionRepository
import com.quqee.backend.internship_hits.public_interface.company_position.CompanyPositionDto
import com.quqee.backend.internship_hits.public_interface.company_position.CreateCompanyPositionDto
import org.springframework.stereotype.Service
import java.util.*

interface CompanyPositionService {
    fun createCompanyPosition(companyId: UUID, createCompanyPositionDto: CreateCompanyPositionDto): CompanyPositionDto
}

@Service
class CompanyPositionServiceImpl (
    private val companyPositionRepository: CompanyPositionRepository
) : CompanyPositionService {
    override fun createCompanyPosition(
        companyId: UUID,
        createCompanyPositionDto: CreateCompanyPositionDto
    ): CompanyPositionDto {
        return companyPositionRepository.createCompanyPosition(companyId, createCompanyPositionDto)
    }

}