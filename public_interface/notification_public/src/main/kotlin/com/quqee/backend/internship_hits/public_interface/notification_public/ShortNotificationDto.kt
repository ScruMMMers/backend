package com.quqee.backend.internship_hits.public_interface.notification_public

import com.quqee.backend.internship_hits.public_interface.common.IHaveId
import com.quqee.backend.internship_hits.public_interface.common.exception.NotificationId
import java.time.OffsetDateTime

data class ShortNotificationDto(
    override val id: NotificationId,
    val title: String,
    val type: NotificationType,
    val description: String,
    val createdAt: OffsetDateTime,
    val isRead: Boolean,
) : IHaveId<NotificationId>
