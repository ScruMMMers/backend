package com.quqee.backend.internship_hits.api.mapper.logs

import com.quqee.backend.internship_hits.mapper.*
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.logs.LogDto
import com.quqee.backend.internship_hits.public_interface.reaction.ReactionDto
import com.quqee.backend.internship_hits.public_interface.tags.TagDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class LogConfigurationMapper(
    private val mapLogType: EnumerationMapper<LogTypeEnum, LogType>,
    private val mapTag: FromApiToInternalMapper<TagView, TagDto>,
    private val mapReaction: FromApiToInternalMapper<ReactionView, ReactionDto>,
    private val mapComment: FromApiToInternalMapper<CommentView, CommentDto>
) {
    @Bean
    fun mapLog(): FromApiToInternalMapper<LogView, LogDto> = makeFromApiMapper { model ->
        LogDto(
            id = model.id.toString(),
            message = model.message,
            tags = model.tags.map { mapTag.fromApi(it) },
            type = mapLogType.mapToInternal(model.type),
            createdAt = model.createdAt,
            editedAt = model.editedAt,
            reactions = model.reactions.map { mapReaction.fromApi(it) },
            comments = model.comments.map { mapComment.fromApi(it) }
        )
    }

    @Bean
    fun mapLogToApi(
        mapTag: FromInternalToApiMapper<TagView, TagDto>,
        mapReaction: FromInternalToApiMapper<ReactionView, ReactionDto>,
        mapComment: FromInternalToApiMapper<CommentView, CommentDto>
    ): FromInternalToApiMapper<LogView, LogDto> = makeToApiMapper { model ->
        LogView(
            id = UUID.fromString(model.id),
            message = model.message,
            tags = model.tags.map { mapTag.fromInternal(it) },
            type = mapLogType.mapToApi(model.type),
            createdAt = model.createdAt,
            editedAt = model.editedAt,
            reactions = model.reactions.map { mapReaction.fromInternal(it) },
            comments = model.comments.map { mapComment.fromInternal(it) }
        )
    }
}