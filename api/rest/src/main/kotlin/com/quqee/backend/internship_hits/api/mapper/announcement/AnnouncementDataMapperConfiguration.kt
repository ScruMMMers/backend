package com.quqee.backend.internship_hits.api.mapper.announcement

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.AnnouncementDataView
import com.quqee.backend.internship_hits.public_interface.announcement_interface.AnnouncementDataDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AnnouncementDataMapperConfiguration {

    @Bean
    fun mapAnnouncementData(): FromApiToInternalMapper<AnnouncementDataView, AnnouncementDataDto> =
        makeFromApiMapper { view ->
            AnnouncementDataDto(
                title = view.title,
                text = view.text,
                pollId = view.pollId
            )
        }

    @Bean
    fun mapAnnouncementDataToApi(): FromInternalToApiMapper<AnnouncementDataView, AnnouncementDataDto> =
        makeToApiMapper { dto ->
            AnnouncementDataView(
                title = dto.title,
                text = dto.text,
                pollId = dto.pollId
            )
        }
}
