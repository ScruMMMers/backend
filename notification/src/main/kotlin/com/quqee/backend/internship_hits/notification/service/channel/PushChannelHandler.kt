package com.quqee.backend.internship_hits.notification.service.channel

import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import com.quqee.backend.internship_hits.public_interface.notification_public.SendNotificationDto
import com.quqee.backend.internship_hits.websocket_common.SessionKey
import com.quqee.backend.internship_hits.websocket_common.WebSocketHandlerType
import com.quqee.backend.internship_hits.websocket_common.WebSocketStorage
import org.springframework.stereotype.Component
import java.util.UUID

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
                description = notification.message,
                title = notification.title,
                type = notification.type,
                id = notification.id,
            )
        )
    }
}

data class PushNotification(
    val id: UUID,
    val type: NotificationType,
    val title: String,
    val description: String,
)