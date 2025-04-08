package com.quqee.backend.internship_hits.api.mapper.tags

import com.quqee.backend.internship_hits.mapper.*
import com.quqee.backend.internship_hits.model.rest.PositionEnum
import com.quqee.backend.internship_hits.model.rest.PositionView
import com.quqee.backend.internship_hits.public_interface.common.PositionDto
import com.quqee.backend.internship_hits.public_interface.common.enums.Position
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PositionConfigurationMapper(
    private val mapPositionName: EnumerationMapper<PositionEnum, Position>
) {

    @Bean
    fun mapPosition(): FromApiToInternalMapper<PositionView, PositionDto> = makeFromApiMapper { model ->
        PositionDto(
            id = model.id,
            name = model.name,
            position = mapPositionName.mapToInternal(model.position)
        )
    }

    @Bean
    fun mapPositionToApi(): FromInternalToApiMapper<PositionView, PositionDto> = makeToApiMapper { model ->
        PositionView(
            id = model.id,
            name = model.name,
            position = mapPositionName.mapToApi(model.position)
        )
    }
}
