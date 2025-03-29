package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.mapper.EnumerationMapper
import com.quqee.backend.internship_hits.model.rest.NotificationTypeEnum
import com.quqee.backend.internship_hits.model.rest.SendNotificationView
import com.quqee.backend.internship_hits.notification.service.NotificationService
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import com.quqee.backend.internship_hits.public_interface.notification_public.SendNotificationDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class DebugController(
    private val notificationService: NotificationService,
    private val notificationTypeMapper: EnumerationMapper<NotificationTypeEnum, NotificationType>,
) : DebugApiDelegate {
    override fun debugWsReceiveNotificationPost(sendNotificationView: SendNotificationView): ResponseEntity<Unit> {
        val userId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.FORBIDDEN)
        val notificationDto = SendNotificationDto(
            userId = userId,
            title = sendNotificationView.title,
            message = sendNotificationView.description,
            type = notificationTypeMapper.mapToInternal(sendNotificationView.type),
            channels = setOf(NotificationChannel.PUSH),
            id = sendNotificationView.id,
        )
        notificationService.sendNotifications(listOf(notificationDto))
        return ResponseEntity.ok().build()
    }
}