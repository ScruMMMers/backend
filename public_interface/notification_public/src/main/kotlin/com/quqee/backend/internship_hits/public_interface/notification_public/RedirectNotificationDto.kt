package com.quqee.backend.internship_hits.public_interface.notification_public

import java.util.*

data class RedirectNotificationDto(
    val redirectId: UUID,
    val fullName: String,
    val avatarUrl: String,
)
