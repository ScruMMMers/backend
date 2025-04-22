package com.quqee.backend.internship_hits.public_interface.notification_public

import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.NotificationId
import java.util.UUID

data class GetUserNotificationsDto(
    val userId: UUID,
    val notificationType: NotificationType,
    val pagination: LastIdPaginationRequest<NotificationId>,
)
