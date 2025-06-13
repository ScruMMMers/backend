package com.quqee.backend.internship_hits.api.mapper.meeting

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.public_interface.common.*
import com.quqee.backend.internship_hits.public_interface.meeting.BuildingDto
import com.quqee.backend.internship_hits.public_interface.meeting.BuildingsListDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BuildingConfigurationMapper {

    @Bean
    fun mapBuildingToApi(): FromInternalToApiMapper <BuildingView, BuildingDto> = makeToApiMapper { model ->
        BuildingView(
            id = model.id,
            name = model.name
        )
    }

    @Bean
    fun mapBuildingsListToApi(
        buildingMapper: FromInternalToApiMapper<BuildingView, BuildingDto>
    ): FromInternalToApiMapper <BuildingsListView, BuildingsListDto> = makeToApiMapper { model ->
        BuildingsListView(
            buildings = model.buildings.map { buildingMapper.fromInternal(it) }
        )
    }
}