package com.quqee.backend.internship_hits.public_interface.comment

import java.util.UUID

data class CreateCommentDto (
    val message: String,
    val replyTo: UUID?
)