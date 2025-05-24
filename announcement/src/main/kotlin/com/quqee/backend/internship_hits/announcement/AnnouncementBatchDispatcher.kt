package com.quqee.backend.internship_hits.announcement

import com.quqee.backend.internship_hits.notification.service.NotificationService
import com.quqee.backend.internship_hits.public_interface.notification_public.CreateNotificationDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
open class AnnouncementBatchDispatcher(
    private val notificationService: NotificationService
) {
    private val log: Logger = LoggerFactory.getLogger(AnnouncementBatchDispatcher::class.java)

    @Async
    open fun sendBatchAsync(
        notificationDtos: List<CreateNotificationDto>,
        onComplete: () -> Unit = {}
    ) {
        try {
            log.info("Отправка пачки ${notificationDtos.size} уведомлений")
            notificationService.createNotifications(notificationDtos)
        } catch (ex: Exception) {
            log.error("Ошибка отправки пачки уведомлений", ex)
        } finally {
            onComplete()
        }
    }
}