package com.quqee.backend.internship_hits.public_interface.common

import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
import java.util.UUID

data class ShortAccountDto(
    val userId: UUID,
    val fullName: String,
    val roles: List<UserRole>,
    val avatarUrl: String,
    val primaryColor: ColorEnum,
    val email: String,
)