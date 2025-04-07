package com.quqee.backend.internship_hits.api.mapper.company_position

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.model.rest.CreateCompanyPositionView
import com.quqee.backend.internship_hits.public_interface.company_position.CreateCompanyPositionDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreateCompanyPositionConfigurationMapper {

    @Bean
    fun mapCreateCompanyPosition(): FromApiToInternalMapper<CreateCompanyPositionView, CreateCompanyPositionDto> =
        makeFromApiMapper { model ->
            CreateCompanyPositionDto(
                name = model.name
            )
        }

}