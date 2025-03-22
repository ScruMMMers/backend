package com.quqee.backend.internship_hits.api.mapper.tags

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.ShortCompanyView
import com.quqee.backend.internship_hits.model.rest.TagView
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.tags.TagDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TagConfigurationMapper(
    private val mapShortCompany: FromApiToInternalMapper<ShortCompanyView, ShortCompanyDto>
) {
    @Bean
    fun mapTag(): FromApiToInternalMapper<TagView, TagDto> = makeFromApiMapper { model ->
        TagDto(
            id = model.id,
            shortCompany = mapShortCompany.fromApi(model.shortCompany),
            name = model.name
        )
    }

    @Bean
    fun mapTagToApi(
        mapShortCompany: FromInternalToApiMapper<ShortCompanyView, ShortCompanyDto>
    ): FromInternalToApiMapper<TagView, TagDto> = makeToApiMapper { model ->
        TagView(
            id = model.id,
            shortCompany = mapShortCompany.fromInternal(model.shortCompany),
            name = model.name
        )
    }
}