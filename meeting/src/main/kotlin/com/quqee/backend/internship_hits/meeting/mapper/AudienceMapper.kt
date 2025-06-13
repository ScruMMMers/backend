package com.quqee.backend.internship_hits.meeting.mapper

import com.quqee.backend.internship_hits.meeting.entity.AudienceEntity
import com.quqee.backend.internship_hits.public_interface.meeting.AudienceDto
import org.springframework.stereotype.Component

@Component
class AudienceMapper(
) {

    fun mapToDto(audienceEntity: AudienceEntity): AudienceDto {
        return AudienceDto(
            audienceEntity.id,
            audienceEntity.name,
            audienceEntity.buildingId
        )
    }

} 