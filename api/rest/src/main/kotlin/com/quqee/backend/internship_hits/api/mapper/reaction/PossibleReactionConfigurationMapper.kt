package com.quqee.backend.internship_hits.api.mapper.reaction

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.PossibleReactionView
import com.quqee.backend.internship_hits.public_interface.reaction.PossibleReactionDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PossibleReactionConfigurationMapper {

    @Bean
    fun mapPossibleReaction(): FromApiToInternalMapper<PossibleReactionView, PossibleReactionDto> =
        makeFromApiMapper { model ->
            PossibleReactionDto(
                id = model.id,
                emoji = model.emoji
            )
        }

    @Bean
    fun mapPossibleReactionToApi(): FromInternalToApiMapper<PossibleReactionView, PossibleReactionDto> =
        makeToApiMapper { model ->
            PossibleReactionView(
                id = model.id,
                emoji = model.emoji
            )
        }
}
