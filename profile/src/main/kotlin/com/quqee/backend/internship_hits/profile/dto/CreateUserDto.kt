package com.quqee.backend.internship_hits.profile.dto

import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole

data class CreateUserDto(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val roles: Set<UserRole>,
    val middleName: String?,
    val photoId: String?,
)
