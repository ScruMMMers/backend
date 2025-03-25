package com.quqee.backend.internship_hits.api.mapper.comment

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.CommentView
import com.quqee.backend.internship_hits.model.rest.ShortAccountView
import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommentConfigurationMapper(
    private val mapShortAccount: FromApiToInternalMapper<ShortAccountView, ShortAccountDto>
) {
    @Bean
    fun mapComment(): FromApiToInternalMapper<CommentView, CommentDto> = makeFromApiMapper { model ->
        CommentDto(
            id = model.id,
            author = mapShortAccount.fromApi(model.author),
            message = model.message,
            replyTo = model.replyTo,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt,
            isDeleted = model.isDeleted
        )
    }

    @Bean
    fun mapCommentToApi(
        mapShortAccount: FromInternalToApiMapper<ShortAccountView, ShortAccountDto>
    ): FromInternalToApiMapper<CommentView, CommentDto> = makeToApiMapper { model ->
        CommentView(
            id = model.id,
            author = mapShortAccount.fromInternal(model.author),
            message = model.message,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt,
            replyTo = model.replyTo,
            isDeleted = model.isDeleted
        )
    }
}
