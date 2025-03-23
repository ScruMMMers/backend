package com.quqee.backend.internship_hits.company.repository

import com.quqee.backend.internship_hits.company.entity.CompanyPositionEntity
import com.quqee.backend.internship_hits.company.mapper.CompanyPositionMapper
import com.quqee.backend.internship_hits.company.repository.jpa.CompanyJpaRepository
import com.quqee.backend.internship_hits.company.repository.jpa.CompanyPositionJpaRepository
import com.quqee.backend.internship_hits.public_interface.company_position.CompanyPositionDto
import com.quqee.backend.internship_hits.public_interface.company_position.CreateCompanyPositionDto
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CompanyPositionRepository(
    private val companyPositionJpaRepository: CompanyPositionJpaRepository,
    private val mapper: CompanyPositionMapper,
    private val companyJpaRepository: CompanyJpaRepository
) {

    /**
     * Создание позиции в компании
     */
    fun createCompanyPosition(createCompanyPositionDto: CreateCompanyPositionDto): CompanyPositionDto {
        // TODO exception
        val companyEntity = companyJpaRepository.findById(createCompanyPositionDto.companyId).orElse(null)

        val companyPositionEntity = CompanyPositionEntity(
            positionId = UUID.randomUUID(),
            company = companyEntity,
            name = createCompanyPositionDto.name,
            employedCount = 0,
            interviewsCount = 0
        )

        return mapper.toCompanyPositionDto(companyPositionJpaRepository.save(companyPositionEntity))
    }

    /**
     * Увеличить счетчик интервью
     */
    fun incrementInterviewsCount(positionId: UUID): CompanyPositionDto {
        // TODO exception
        val positionEntity = companyPositionJpaRepository.findById(positionId).orElse(null)

        positionEntity.interviewsCount++

        return mapper.toCompanyPositionDto(companyPositionJpaRepository.save(positionEntity))
    }

    /**
     * Увеличить счетчик принятых на стажировку
     */
    fun incrementEmployedCount(positionId: UUID): CompanyPositionDto {
        // TODO exception
        val positionEntity = companyPositionJpaRepository.findById(positionId).orElse(null)

        positionEntity.employedCount++

        return mapper.toCompanyPositionDto(companyPositionJpaRepository.save(positionEntity))
    }

}