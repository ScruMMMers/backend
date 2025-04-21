package com.quqee.backend.internship_hits.api.mapper.comment

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.CommentWithoutRepliesView
import com.quqee.backend.internship_hits.model.rest.ShortAccountView
import com.quqee.backend.internship_hits.public_interface.common.CommentWithoutRepliesDto
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommentWithoutRepliesConfigurationMapper(
    private val mapShortAccount: FromApiToInternalMapper<ShortAccountView, ShortAccountDto>
) {
    @Bean
    fun mapCommentWithoutReplies(): FromApiToInternalMapper<CommentWithoutRepliesView, CommentWithoutRepliesDto> = makeFromApiMapper { model ->
        CommentWithoutRepliesDto(
            id = model.id,
            author = mapShortAccount.fromApi(model.author),
            message = model.message,
            logId = model.logId,
            replyTo = model.replyTo,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt,
            isDeleted = model.isDeleted
        )
    }

    @Bean
    fun mapCommentWithoutRepliesToApi(
        mapShortAccount: FromInternalToApiMapper<ShortAccountView, ShortAccountDto>
    ): FromInternalToApiMapper<CommentWithoutRepliesView, CommentWithoutRepliesDto> = makeToApiMapper { model ->
        CommentWithoutRepliesView(
            id = model.id,
            author = mapShortAccount.fromInternal(model.author),
            message = model.message,
            createdAt = model.createdAt,
            updatedAt = model.updatedAt,
            replyTo = model.replyTo,
            logId = model.logId,
            isDeleted = model.isDeleted
        )
    }
}
