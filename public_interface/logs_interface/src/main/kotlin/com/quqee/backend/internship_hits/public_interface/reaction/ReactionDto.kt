package com.quqee.backend.internship_hits.public_interface.reaction

import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto

data class ReactionDto(
    val shortAccount: ShortAccountDto,
    val emoji: String
)
