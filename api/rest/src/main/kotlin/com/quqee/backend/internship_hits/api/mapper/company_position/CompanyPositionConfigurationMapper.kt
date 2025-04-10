package com.quqee.backend.internship_hits.api.mapper.company_position

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.CompanyPositionView
import com.quqee.backend.internship_hits.model.rest.PositionView
import com.quqee.backend.internship_hits.model.rest.ShortAccountView
import com.quqee.backend.internship_hits.public_interface.common.PositionDto
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.company_position.CompanyPositionDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CompanyPositionConfigurationMapper {

    @Bean
    fun mapCompanyPosition(
        positionMapper: FromApiToInternalMapper<PositionView, PositionDto>,
    ): FromApiToInternalMapper<CompanyPositionView, CompanyPositionDto> =
        makeFromApiMapper { model ->
            CompanyPositionDto(
                id = model.id,
                companyId = model.companyId,
                position = positionMapper.fromApi(model.position)
            )
        }

    @Bean
    fun mapCompanyPositionToApi(
        positionMapper: FromInternalToApiMapper<PositionView, PositionDto>,
    ): FromInternalToApiMapper<CompanyPositionView, CompanyPositionDto> =
        makeToApiMapper { model ->
            CompanyPositionView(
                id = model.id,
                companyId = model.companyId,
                position = positionMapper.fromInternal(model.position)
            )
        }

}