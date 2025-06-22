package com.quqee.backend.internship_hits.company.mapper

import com.quqee.backend.internship_hits.company.entity.CompanyPositionEntity
import com.quqee.backend.internship_hits.position.service.PositionService
import com.quqee.backend.internship_hits.public_interface.company_position.CompanyPositionDto
import org.springframework.stereotype.Component

@Component
class CompanyPositionMapper(
    private val positionService: PositionService,
) {

    /**
     * Преобразование сущности позиции в компании в DTO
     */
    fun toCompanyPositionDto(entity: CompanyPositionEntity): CompanyPositionDto {
        return CompanyPositionDto(
            id = entity.id,
            companyId = entity.company.companyId,
            position = positionService.getPositionById(entity.positionId),
            employedCount = positionService.getEmployedCount(entity.company.companyId, entity.positionId),
            interviewsCount = entity.interviewsCount
        )
    }

}