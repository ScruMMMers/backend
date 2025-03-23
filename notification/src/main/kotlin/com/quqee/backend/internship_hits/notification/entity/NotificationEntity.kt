package com.quqee.backend.internship_hits.notification.entity

import com.quqee.backend.internship_hits.notification.public.Public
import com.quqee.backend.internship_hits.public_interface.common.exception.NotificationId
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import java.time.OffsetDateTime
import java.util.UUID

data class NotificationEntity(
    val id: NotificationId,
    val title: String,
    val message: String,
    val createdAt: OffsetDateTime,
    val isRead: Boolean,
    val userId: UUID,
    val type: NotificationType,
    val channels: Set<NotificationChannel>,
    val attachment: NotificationAttachmentEntity?,
) {
    companion object {
        val FIELDS = listOf(
            Public.PUBLIC.NOTIFICATION.ID,
            Public.PUBLIC.NOTIFICATION.TITLE,
            Public.PUBLIC.NOTIFICATION.MESSAGE,
            Public.PUBLIC.NOTIFICATION.CREATED_AT,
            Public.PUBLIC.NOTIFICATION.IS_READ,
            Public.PUBLIC.NOTIFICATION.RECEIVER_ID,
            Public.PUBLIC.NOTIFICATION.TYPE,
            Public.PUBLIC.NOTIFICATION.CHANNELS,
            Public.PUBLIC.NOTIFICATION.ATTACHMENT,
        )
    }
}
