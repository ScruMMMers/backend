package com.quqee.backend.internship_hits.public_interface.notification_public

import java.util.UUID

data class SendNotificationDto(
    val title: String,
    val message: String,
    val userId: UUID,
    val channels: Set<NotificationChannel>,
)
