package com.quqee.backend.internship_hits.public_interface.common

import java.util.UUID

data class AudienceDto(
    val id: UUID,
    val name: String,
    val buildingId: UUID
)
