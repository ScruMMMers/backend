package com.quqee.backend.internship_hits.api.mapper.company

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.model.rest.CreateCompanyView
import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import com.quqee.backend.internship_hits.public_interface.company.CreateCompanyDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreateCompanyConfigurationMapper {

    @Bean
    fun mapCreateCompany(): FromApiToInternalMapper<CreateCompanyView, CreateCompanyDto> = makeFromApiMapper { model ->
        CreateCompanyDto(
            name = model.name,
            sinceYear = model.sinceYear,
            avatarId = model.avatarId,
            agentId = model.agentId,
            description = model.description,
            primaryColor = ColorEnum.fromHex(model.primaryColor)
        )
    }
}