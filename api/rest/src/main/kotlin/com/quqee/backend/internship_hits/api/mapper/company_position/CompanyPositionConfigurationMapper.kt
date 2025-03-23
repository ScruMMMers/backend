package com.quqee.backend.internship_hits.api.mapper.company_position

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.CompanyPositionView
import com.quqee.backend.internship_hits.public_interface.company_position.CompanyPositionDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CompanyPositionConfigurationMapper {

    @Bean
    fun mapCompanyPosition(): FromApiToInternalMapper<CompanyPositionView, CompanyPositionDto> =
        makeFromApiMapper { model ->
            CompanyPositionDto(
                positionId = model.positionId,
                companyId = model.positionId,
                name = model.name,
                employedCount = model.employedCount,
                interviewsCount = model.interviewsCount
            )
        }

    @Bean
    fun mapCompanyPositionToApi(): FromInternalToApiMapper<CompanyPositionView, CompanyPositionDto> =
        makeToApiMapper { model ->
            CompanyPositionView(
                positionId = model.positionId,
                companyId = model.companyId,
                name = model.name,
                employedCount = model.employedCount,
                interviewsCount = model.interviewsCount
            )
        }

}