package com.quqee.backend.internship_hits.public_interface.notification_public

import com.quqee.backend.internship_hits.public_interface.common.NotificationId
import com.quqee.backend.internship_hits.public_interface.common.UserId

data class ReadNotificationsDto(
    val notificationIds: List<NotificationId>,
    val userId: UserId
)
