package com.quqee.backend.internship_hits.public_interface.mark

import java.time.OffsetDateTime
import java.util.UUID

data class MarkDto(
    val id: UUID,
    val userId: UUID,
    val mark: Int,
    val date: OffsetDateTime,
    val semester: Int,
)
