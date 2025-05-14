package com.quqee.backend.internship_hits.api.mapper.company

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.ShortCompanyWithEmployersView
import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import com.quqee.backend.internship_hits.public_interface.company.ShortCompanyWithEmployersDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ShortCompanyWithEmployersConfigurationMapper {
    @Bean
    fun mapShortCompanyWithEmployers(): FromApiToInternalMapper<ShortCompanyWithEmployersView, ShortCompanyWithEmployersDto> = makeFromApiMapper { model ->
        ShortCompanyWithEmployersDto(
            companyId = model.companyId,
            name = model.name,
            avatarUrl = model.avatarUrl,
            primaryColor = ColorEnum.fromHex(model.primaryColor),
            sinceYear = model.sinceYear,
            description = model.description,
            employedCount = model.employedCount,
        )
    }

    @Bean
    fun mapShortCompanyWithEmployersToApi(): FromInternalToApiMapper<ShortCompanyWithEmployersView, ShortCompanyWithEmployersDto> = makeToApiMapper { model ->
        ShortCompanyWithEmployersView(
            companyId = model.companyId,
            name = model.name,
            avatarUrl = model.avatarUrl,
            primaryColor = model.primaryColor.hexColor,
            sinceYear = model.sinceYear,
            description = model.description,
            employedCount = model.employedCount,
        )
    }
}