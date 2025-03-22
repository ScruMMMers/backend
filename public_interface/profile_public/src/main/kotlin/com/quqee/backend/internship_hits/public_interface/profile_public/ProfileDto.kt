package com.quqee.backend.internship_hits.public_interface.profile_public

import com.quqee.backend.internship_hits.public_interface.common.exception.UserRole
import java.util.UUID

data class ProfileDto(
    val userId: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val username: String,
    val roles: Collection<UserRole>,
    val avatarUrl: String?,
)
