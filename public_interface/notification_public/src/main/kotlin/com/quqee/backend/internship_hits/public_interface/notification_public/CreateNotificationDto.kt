package com.quqee.backend.internship_hits.public_interface.notification_public

import java.util.*

data class CreateNotificationDto(
    val title: String,
    val message: String,
    val userId: UUID,
    val type: NotificationType,
    val channels: Set<NotificationChannel>,
    val pollId: UUID? = null,
)
