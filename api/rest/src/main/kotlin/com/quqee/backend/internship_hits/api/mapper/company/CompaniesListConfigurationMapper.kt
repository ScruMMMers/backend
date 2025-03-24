package com.quqee.backend.internship_hits.api.mapper.company

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.company.CompaniesListDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CompaniesListConfigurationMapper (
    private val mapPagination: FromInternalToApiMapper<LastIdPaginationView, LastIdPagination>
) {

    @Bean
    fun mapCompaniesListToApi(
        companyMapper: FromInternalToApiMapper<ShortCompanyView, ShortCompanyDto>
    ): FromInternalToApiMapper<CompaniesListView, CompaniesListDto> = makeToApiMapper { model ->
        CompaniesListView(
            companies = model.companies.map { companyMapper.fromInternal(it) },
            page = mapPagination.fromInternal(model.page)
        )
    }

}