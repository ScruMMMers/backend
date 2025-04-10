package com.quqee.backend.internship_hits.company.repository

import com.quqee.backend.internship_hits.company.entity.CompanyPositionEntity
import com.quqee.backend.internship_hits.company.mapper.CompanyPositionMapper
import com.quqee.backend.internship_hits.company.repository.jpa.CompanyJpaRepository
import com.quqee.backend.internship_hits.company.repository.jpa.CompanyPositionJpaRepository
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.company_position.CompanyPositionDto
import com.quqee.backend.internship_hits.public_interface.company_position.CreateCompanyPositionDto
import org.springframework.stereotype.Component
import java.util.*

@Component
class CompanyPositionRepository(
    private val companyPositionJpaRepository: CompanyPositionJpaRepository,
    private val mapper: CompanyPositionMapper,
    private val companyJpaRepository: CompanyJpaRepository
) {

    /**
     * Создание позиции в компании
     */
    fun createCompanyPosition(companyId: UUID, createCompanyPositionDto: CreateCompanyPositionDto): CompanyPositionDto {
        val companyEntity = companyJpaRepository.findById(companyId)
            .orElseThrow {
                ExceptionInApplication(
                    ExceptionType.NOT_FOUND,
                    "Компания не найдена с идентификатором $createCompanyPositionDto.companyId"
                )
            }

        val companyPositionEntity = CompanyPositionEntity(
            id = UUID.randomUUID(),
            company = companyEntity,
            positionId = createCompanyPositionDto.positionId
        )

        return mapper.toCompanyPositionDto(companyPositionJpaRepository.save(companyPositionEntity))
    }

}