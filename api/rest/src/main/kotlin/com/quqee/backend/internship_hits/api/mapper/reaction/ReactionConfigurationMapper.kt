package com.quqee.backend.internship_hits.api.mapper.reaction

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.ReactionView
import com.quqee.backend.internship_hits.model.rest.ShortAccountView
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.reaction.ReactionDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ReactionConfigurationMapper(
    private val mapShortAccount: FromApiToInternalMapper<ShortAccountView, ShortAccountDto>
) {
    @Bean
    fun mapReaction(): FromApiToInternalMapper<ReactionView, ReactionDto> = makeFromApiMapper { model ->
        ReactionDto(
            shortAccount = mapShortAccount.fromApi(model.shortAccount),
            emoji = model.emoji
        )
    }

    @Bean
    fun mapReactionToApi(
        mapShortAccount: FromInternalToApiMapper<ShortAccountView, ShortAccountDto>
    ): FromInternalToApiMapper<ReactionView, ReactionDto> = makeToApiMapper { model ->
        ReactionView(
            shortAccount = mapShortAccount.fromInternal(model.shortAccount),
            emoji = model.emoji
        )
    }
}
