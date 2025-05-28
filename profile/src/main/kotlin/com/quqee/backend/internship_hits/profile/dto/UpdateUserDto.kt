package com.quqee.backend.internship_hits.profile.dto

import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole

data class UpdateUserDto(
    val userId: UserId,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val roles: Set<UserRole>,
    val middleName: String?,
    val photoId: String?,
    val isEmailChanged: Boolean = true,
    val isUsernameChanged: Boolean = true,
)
