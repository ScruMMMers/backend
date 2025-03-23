package com.quqee.backend.internship_hits.public_interface.notification_public

import java.util.*

data class GetUserLastNotificationDto(
    val userId: UUID,
    val notificationType: NotificationType,
)
