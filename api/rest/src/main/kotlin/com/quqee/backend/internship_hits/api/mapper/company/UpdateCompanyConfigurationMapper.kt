package com.quqee.backend.internship_hits.api.mapper.company

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.model.rest.UpdateCompanyView
import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import com.quqee.backend.internship_hits.public_interface.company.UpdateCompanyDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UpdateCompanyConfigurationMapper {

    @Bean
    fun mapUpdateCompany(): FromApiToInternalMapper<UpdateCompanyView, UpdateCompanyDto> = makeFromApiMapper { model ->
        UpdateCompanyDto(
            name = model.name,
            sinceYear = model.sinceYear,
            avatarId = model.avatarId,
            agentId = model.agentId,
            description = model.description,
            primaryColor = model.primaryColor?.let { ColorEnum.fromHex(it) }
        )
    }
}