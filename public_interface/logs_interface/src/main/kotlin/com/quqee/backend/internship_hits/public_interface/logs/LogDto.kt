package com.quqee.backend.internship_hits.public_interface.logs

import com.quqee.backend.internship_hits.public_interface.common.*
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.reaction.ReactionDto
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import java.time.OffsetDateTime
import java.util.UUID

data class LogDto(
    val id: UUID,
    val message: String,
    val tags: List<TagDto>,
    val hashtags: List<PositionDto>,
    val type: LogType,
    val createdAt: OffsetDateTime,
    val editedAt: OffsetDateTime?,
    val reactions: List<ReactionDto>,
    val comments: List<CommentDto>,
    val author: ShortAccountDto,
    val files: List<FileDto>,
    val approvalStatus: ApprovalStatus
)
