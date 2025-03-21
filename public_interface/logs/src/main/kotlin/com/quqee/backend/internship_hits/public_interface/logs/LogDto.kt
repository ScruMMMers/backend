package com.quqee.backend.internship_hits.public_interface.logs

import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.enums.LogTypeEnum
import com.quqee.backend.internship_hits.public_interface.reaction.ReactionDto
import com.quqee.backend.internship_hits.public_interface.tags.TagDto
import java.time.OffsetDateTime

data class LogDto(
    val id: String,
    val message: String,
    val tags: List<TagDto>,
    val type: LogTypeEnum,
    val createdAt: OffsetDateTime,
    val editedAt: OffsetDateTime,
    val reactions: List<ReactionDto>,
    val comments: List<CommentDto>
)
