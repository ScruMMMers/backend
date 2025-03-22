package com.quqee.backend.internship_hits.notification

import com.quqee.backend.internship_hits.public_interface.notification_public.GetNotificationForHeaderDto
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import com.quqee.backend.internship_hits.public_interface.notification_public.ShortNotificationDto
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

@Service
class NotificationService {
    fun getNotificationsForHeader(dto: GetNotificationForHeaderDto): List<ShortNotificationDto> {
        return listOf(
            ShortNotificationDto(
                notificationId = UUID.randomUUID(),
                title = "Бабанов объявлет бомбандировку",
                type = NotificationType.DEANERY,
                description = "Ну почему вы такие тупые?",
                createdAt = OffsetDateTime.now(),
                isRead = false,
            )
        )
    }
}