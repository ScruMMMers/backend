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
    fun createCompanyPosition(companyId :UUID, createCompanyPositionDto: CreateCompanyPositionDto): CompanyPositionDto {
        val companyEntity = companyJpaRepository.findById(companyId)
            .orElseThrow {
                ExceptionInApplication(
                    ExceptionType.NOT_FOUND,
                    "Компания не найдена с идентификатором $createCompanyPositionDto.companyId"
                )
            }

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
        val positionEntity = companyPositionJpaRepository.findById(positionId)
            .orElseThrow {
                ExceptionInApplication(
                    ExceptionType.NOT_FOUND,
                    "Позиция в компании не найдена с идентификатором $positionId"
                )
            }

        positionEntity.interviewsCount++

        return mapper.toCompanyPositionDto(companyPositionJpaRepository.save(positionEntity))
    }

    /**
     * Увеличить счетчик принятых на стажировку
     */
    fun incrementEmployedCount(positionId: UUID): CompanyPositionDto {
        val positionEntity = companyPositionJpaRepository.findById(positionId)
            .orElseThrow {
                ExceptionInApplication(
                    ExceptionType.NOT_FOUND,
                    "Позиция в компании не найдена с идентификатором $positionId"
                )
            }

        positionEntity.employedCount++

        return mapper.toCompanyPositionDto(companyPositionJpaRepository.save(positionEntity))
    }

}