package com.quqee.backend.internship_hits.profile.dto

import com.quqee.backend.internship_hits.public_interface.common.UserId

data class UpdateAvatarDto(
    val userId: UserId,
    val avatarId: String?
)
