package com.quqee.backend.internship_hits.public_interface.common

import java.time.OffsetDateTime

data class CommentDto(
    val id: String,
    val shortAccount: ShortAccountDto,
    val message: String,
    val replyTo: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime
)
