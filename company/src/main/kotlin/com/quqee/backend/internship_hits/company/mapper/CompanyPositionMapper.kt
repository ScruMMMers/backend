package com.quqee.backend.internship_hits.company.mapper

import com.quqee.backend.internship_hits.company.entity.CompanyPositionEntity
import com.quqee.backend.internship_hits.public_interface.company_position.CompanyPositionDto
import org.springframework.stereotype.Component

@Component
class CompanyPositionMapper(
) {

    /**
     * Преобразование сущности позиции в компании в DTO
     */
    fun toCompanyPositionDto(entity: CompanyPositionEntity): CompanyPositionDto {
        return CompanyPositionDto(
            positionId = entity.positionId,
            companyId = entity.company.companyId,
            name = entity.name,
            employedCount = entity.employedCount,
            interviewsCount = entity.interviewsCount
        )
    }

}