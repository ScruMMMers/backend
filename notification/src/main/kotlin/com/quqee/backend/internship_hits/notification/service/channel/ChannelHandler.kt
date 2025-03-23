package com.quqee.backend.internship_hits.notification.service.channel

import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.SendNotificationDto

abstract class ChannelHandler(
    val channelType: NotificationChannel
) {
    abstract fun handle(notification: SendNotificationDto)
}