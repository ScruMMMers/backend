package com.quqee.backend.internship_hits.public_interface.reaction

import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import java.util.*

data class ReactionDto(
    val id: UUID,
    val shortAccount: ShortAccountDto,
    val emoji: String
)
