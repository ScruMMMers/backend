package com.quqee.backend.internship_hits.meeting.mapper

import com.quqee.backend.internship_hits.meeting.entity.BuildingEntity
import com.quqee.backend.internship_hits.public_interface.common.AudienceDto
import com.quqee.backend.internship_hits.public_interface.common.BuildingDto
import org.springframework.stereotype.Component

@Component
class BuildingMapper(
) {

    fun mapToDto(buildingEntity: BuildingEntity): BuildingDto {
        return BuildingDto(
            buildingEntity.id,
            buildingEntity.name,
        )
    }

} 