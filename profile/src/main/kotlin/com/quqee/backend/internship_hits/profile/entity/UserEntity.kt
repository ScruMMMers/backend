package com.quqee.backend.internship_hits.profile.entity

import com.quqee.backend.internship_hits.profile.dto.UpdateUserDto
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
import java.util.*

data class UserEntity(
    val userId: UUID,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val roles: Set<UserRole>,
    val middleName: String?,
    val photoId: String?,
) {
    fun toUpdateDto(
        userName: String? = null,
        email: String? = null,
        firstName: String? = null,
        lastName: String? = null,
        roles: Set<UserRole>? = null,
        middleName: String? = null,
        photoId: String? = null,
    ): UpdateUserDto {
        return UpdateUserDto(
            userId = userId,
            username = userName ?: this.username,
            email = email ?: this.email,
            firstName = firstName ?: this.firstName,
            lastName = lastName ?: this.lastName,
            roles = roles ?: this.roles,
            middleName = middleName,
            photoId = photoId,
            isEmailChanged = this.email != email,
            isUsernameChanged = this.username != userName,
        )
    }
}
