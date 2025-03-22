package com.quqee.backend.internship_hits.mapper.logs

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.LogView
import com.quqee.backend.internship_hits.public_interface.logs.LogDto
import org.springframework.stereotype.Component
import java.util.*
import kotlin.reflect.KClass

@Component
class LogMapper(
    private val tagMapper: TagMapper,
    private val reactionMapper: ReactionMapper,
    private val commentMapper: CommentMapper
) : FromApiToInternalMapper<LogView, LogDto>, FromInternalToApiMapper<LogView, LogDto> {

    override fun fromApi(item: LogView): LogDto {
        return LogDto(
            id = item.id.toString(),
            message = item.message,
            tags = item.tags.map { tagMapper.fromApi(it) },
            type = item.type,
            createdAt = item.createdAt,
            editedAt = item.editedAt,
            reactions = item.reactions.map { reactionMapper.fromApi(it) },
            comments = item.comments.map { commentMapper.fromApi(it) }
        )
    }

    override fun fromInternal(item: LogDto): LogView {
        return LogView(
            id = UUID.fromString(item.id),
            message = item.message,
            tags = item.tags.map { tagMapper.toApi(it) },
            type = item.type,
            createdAt = item.createdAt,
            editedAt = item.editedAt,
            reactions = item.reactions.map { reactionMapper.toApi(it) },
            comments = item.comments.map { commentMapper.toApi(it) }
        )
    }

    override fun apiType(): KClass<LogView> = LogView::class
    override fun internalType(): KClass<LogDto> = LogDto::class
}
