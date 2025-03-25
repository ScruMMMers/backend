package com.quqee.backend.internship_hits.notification.service.channel

import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.SendNotificationDto
import com.quqee.backend.internship_hits.websocket_common.SessionKey
import com.quqee.backend.internship_hits.websocket_common.WebSocketHandlerType
import com.quqee.backend.internship_hits.websocket_common.WebSocketStorage
import org.springframework.stereotype.Component

@Component
class PushChannelHandler(
    private val webSocketStorage: WebSocketStorage
) : ChannelHandler(
    NotificationChannel.PUSH
) {
    override fun handle(notification: SendNotificationDto) {
        webSocketStorage.sendMessage(
            SessionKey(
                notification.userId,
                WebSocketHandlerType.NOTIFICATIONS
            ),
            PushNotification(
                message = notification.message,
                title = notification.title
            )
        )
    }
}

data class PushNotification(
    val message: String,
    val title: String,
)