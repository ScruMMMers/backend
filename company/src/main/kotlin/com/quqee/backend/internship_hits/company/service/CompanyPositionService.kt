package com.quqee.backend.internship_hits.company.service

import com.quqee.backend.internship_hits.company.repository.CompanyPositionRepository
import com.quqee.backend.internship_hits.public_interface.company_position.CompanyPositionDto
import com.quqee.backend.internship_hits.public_interface.company_position.CreateCompanyPositionDto
import org.springframework.stereotype.Service
import java.util.*

interface CompanyPositionService {
    fun createCompanyPosition(createCompanyPositionDto: CreateCompanyPositionDto): CompanyPositionDto
    fun incrementInterviewsCount(positionId: UUID): CompanyPositionDto
    fun incrementEmployedCountCount(positionId: UUID): CompanyPositionDto
}

@Service
class CompanyPositionServiceImpl (
    private val companyPositionRepository: CompanyPositionRepository
) : CompanyPositionService {
    override fun createCompanyPosition(createCompanyPositionDto: CreateCompanyPositionDto): CompanyPositionDto {
        return companyPositionRepository.createCompanyPosition(createCompanyPositionDto)
    }

    override fun incrementInterviewsCount(positionId: UUID): CompanyPositionDto {
        return companyPositionRepository.incrementInterviewsCount(positionId)
    }

    override fun incrementEmployedCountCount(positionId: UUID): CompanyPositionDto {
        return companyPositionRepository.incrementEmployedCount(positionId)
    }

}