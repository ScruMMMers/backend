package com.quqee.backend.internship_hits.notification.service.channel

import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.SendNotificationDto
import org.springframework.stereotype.Component

@Component
class PushChannelHandler : ChannelHandler(
    NotificationChannel.PUSH
) {
    override fun handle(notification: SendNotificationDto) {
    }
}