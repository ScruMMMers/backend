package com.quqee.backend.internship_hits.api.mapper.company

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.ShortCompanyView
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ShortCompanyConfigurationMapper {
    @Bean
    fun mapShortCompany(): FromApiToInternalMapper<ShortCompanyView, ShortCompanyDto> = makeFromApiMapper { model ->
        ShortCompanyDto(
            companyId = model.companyId,
            name = model.name,
            avatarUrl = model.avatarUrl,
            primaryColor = ColorEnum.fromHex(model.primaryColor),
            createdAt = model.createdAt
        )
    }

    @Bean
    fun mapShortCompanyToApi(): FromInternalToApiMapper<ShortCompanyView, ShortCompanyDto> = makeToApiMapper { model ->
        ShortCompanyView(
            companyId = model.companyId,
            name = model.name,
            avatarUrl = model.avatarUrl,
            primaryColor = model.primaryColor.hexColor,
            createdAt = model.createdAt
        )
    }
}