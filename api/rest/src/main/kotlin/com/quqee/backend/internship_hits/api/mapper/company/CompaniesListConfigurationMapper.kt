package com.quqee.backend.internship_hits.api.mapper.company

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.CompaniesListView
import com.quqee.backend.internship_hits.model.rest.LastIdPaginationView
import com.quqee.backend.internship_hits.model.rest.ShortCompanyWithEmployersView
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import com.quqee.backend.internship_hits.public_interface.company.CompaniesListDto
import com.quqee.backend.internship_hits.public_interface.company.ShortCompanyWithEmployersDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CompaniesListConfigurationMapper (
    private val mapPagination: FromInternalToApiMapper<LastIdPaginationView, LastIdPagination>
) {

    @Bean
    fun mapCompaniesListToApi(
        companyMapper: FromInternalToApiMapper<ShortCompanyWithEmployersView, ShortCompanyWithEmployersDto>
    ): FromInternalToApiMapper<CompaniesListView, CompaniesListDto> = makeToApiMapper { model ->
        CompaniesListView(
            companies = model.companies.map { companyMapper.fromInternal(it) },
            page = mapPagination.fromInternal(model.page)
        )
    }

}