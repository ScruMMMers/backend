package com.quqee.backend.internship_hits.public_interface.reaction

import java.util.UUID

data class PossibleReactionDto(
    val id: UUID,
    val emoji: String
)