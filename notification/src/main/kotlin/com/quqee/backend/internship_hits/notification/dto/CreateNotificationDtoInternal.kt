package com.quqee.backend.internship_hits.notification.dto

import com.quqee.backend.internship_hits.notification.entity.NotificationAttachmentEntity
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import java.time.OffsetDateTime
import java.util.*

data class CreateNotificationDtoInternal(
    val title: String,
    val message: String,
    val createdAt: OffsetDateTime,
    val isRead: Boolean,
    val userId: UUID,
    val type: NotificationType,
    val attachment: NotificationAttachmentEntity?,
    val channels: Set<NotificationChannel>,
)