package com.quqee.backend.internship_hits.api.mapper.meeting

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.AudienceView
import com.quqee.backend.internship_hits.model.rest.AudiencesListView
import com.quqee.backend.internship_hits.public_interface.common.AudienceDto
import com.quqee.backend.internship_hits.public_interface.common.AudiencesListDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AudienceConfigurationMapper {

    @Bean
    fun mapAudienceToApi(): FromInternalToApiMapper <AudienceView, AudienceDto> = makeToApiMapper { model ->
        AudienceView(
            id = model.id,
            name = model.name,
            buildingId = model.buildingId
        )
    }

    @Bean
    fun mapAudiencesListToApi(
        audienceMapper: FromInternalToApiMapper<AudienceView, AudienceDto>
    ): FromInternalToApiMapper <AudiencesListView, AudiencesListDto> = makeToApiMapper { model ->
        AudiencesListView(
            audiences = model.audiences.map { audienceMapper.fromInternal(it) }
        )
    }
}