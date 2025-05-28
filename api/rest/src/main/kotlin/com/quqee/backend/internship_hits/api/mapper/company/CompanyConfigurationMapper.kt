package com.quqee.backend.internship_hits.api.mapper.company

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.CompanyPositionView
import com.quqee.backend.internship_hits.model.rest.CompanyView
import com.quqee.backend.internship_hits.model.rest.ShortAccountView
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.company.CompanyDto
import com.quqee.backend.internship_hits.public_interface.company_position.CompanyPositionDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CompanyConfigurationMapper {

    @Bean
    fun mapCompany(
        accountMapper: FromApiToInternalMapper<ShortAccountView, ShortAccountDto>,
        companyPositionMapper: FromApiToInternalMapper<CompanyPositionView, CompanyPositionDto>
    ): FromApiToInternalMapper<CompanyView, CompanyDto> = makeFromApiMapper { model ->
        CompanyDto(
            companyId = model.companyId,
            name = model.name,
            agent = model.agent?.let { accountMapper.fromApi(it) },
            avatarUrl = model.avatarUrl,
            sinceYear = model.sinceYear,
            description = model.description,
            primaryColor = model.primaryColor,
            positions = model.positions.map { companyPositionMapper.fromApi(it) },
            createdAt = model.createdAt
        )
    }

    @Bean
    fun mapCompanyToApi(
        accountMapper: FromInternalToApiMapper<ShortAccountView, ShortAccountDto>,
        companyPositionMapper: FromInternalToApiMapper<CompanyPositionView, CompanyPositionDto>
    ): FromInternalToApiMapper <CompanyView, CompanyDto> = makeToApiMapper { model ->
        CompanyView(
            companyId = model.companyId,
            name = model.name,
            agent = model.agent?.let { accountMapper.fromInternal(it) },
            avatarUrl = model.avatarUrl,
            sinceYear = model.sinceYear,
            description = model.description,
            primaryColor = model.primaryColor,
            positions = model.positions.map { companyPositionMapper.fromInternal(it) },
            createdAt = model.createdAt
        )
    }
}