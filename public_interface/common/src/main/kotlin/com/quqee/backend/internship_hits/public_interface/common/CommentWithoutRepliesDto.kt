package com.quqee.backend.internship_hits.public_interface.common

import java.time.OffsetDateTime
import java.util.UUID

data class CommentWithoutRepliesDto(
    val id: UUID,
    val author: ShortAccountDto,
    val message: String,
    val replyTo: UUID?,
    val logId: UUID,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime?,
    val isDeleted: Boolean
)
