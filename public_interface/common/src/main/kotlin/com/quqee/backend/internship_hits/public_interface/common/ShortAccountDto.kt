package com.quqee.backend.internship_hits.public_interface.common

import com.quqee.backend.internship_hits.public_interface.common.enums.Role

data class ShortAccountDto(
    val userId: String,
    val fullName: String,
    val roles: List<Role>,
    val avatarUrl: String,
    val primaryColor: String
)