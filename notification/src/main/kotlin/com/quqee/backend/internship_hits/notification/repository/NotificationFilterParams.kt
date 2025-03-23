package com.quqee.backend.internship_hits.notification.repository

import com.quqee.backend.internship_hits.public_interface.common.exception.NotificationId
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import java.util.UUID

data class NotificationFilterParams(
    val ids: Set<NotificationId>? = null,
    val type: NotificationType? = null,
    val userId: UUID? = null,
    val isRead: Boolean? = null,
)