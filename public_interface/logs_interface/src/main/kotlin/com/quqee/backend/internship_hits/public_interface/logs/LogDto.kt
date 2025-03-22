package com.quqee.backend.internship_hits.public_interface.logs

import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.reaction.ReactionDto
import com.quqee.backend.internship_hits.public_interface.tags.TagDto
import java.time.OffsetDateTime
import java.util.UUID

data class LogDto(
    val id: UUID,
    val message: String,
    val tags: List<TagDto>,
    val type: LogType,
    val createdAt: OffsetDateTime,
    val editedAt: OffsetDateTime,
    val reactions: List<ReactionDto>,
    val comments: List<CommentDto>
)
