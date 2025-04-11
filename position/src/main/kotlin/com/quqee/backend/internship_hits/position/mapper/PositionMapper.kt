package com.quqee.backend.internship_hits.position.mapper

import com.quqee.backend.internship_hits.position.entity.PositionEntity
import com.quqee.backend.internship_hits.public_interface.common.PositionDto
import org.springframework.stereotype.Component

@Component
class PositionMapper {

    fun toDto(entity: PositionEntity): PositionDto {
        return PositionDto(
            id = entity.id,
            name = entity.name,
            position = entity.position
        )
    }

}