package com.quqee.backend.internship_hits.profile.entity

import com.quqee.backend.internship_hits.public_interface.common.exception.UserRole
import java.util.*

data class UserEntity(
    val userId: UUID,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val roles: Set<UserRole>,
)
