package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.mapper.EnumerationMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.notification.service.NotificationService
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationResponse
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.notification_public.GetUserLastNotificationDto
import com.quqee.backend.internship_hits.public_interface.notification_public.GetUserNotificationsDto
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationDto
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class NotificationsController(
    private val notificationService: NotificationService,
    private val notificationMapper: FromInternalToApiMapper<NotificationView, NotificationDto>,
    private val notificationTypeMapper: EnumerationMapper<NotificationTypeEnum, NotificationType>,
    private val lastIdPaginationResponseMapper: FromInternalToApiMapper<LastIdPaginationView, LastIdPaginationResponse<*, UUID>>,
) : NotificationsApiDelegate {
    override fun notificationsGet(
        type: NotificationTypeEnum?,
        lastId: UUID?,
        size: Int?
    ): ResponseEntity<GetNotificationsResponseView> {
        val userId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.FORBIDDEN)
        val notifications = notificationService.getNotifications(
            GetUserNotificationsDto(
                userId = userId,
                notificationType = notificationTypeMapper.mapToInternal(type ?: NotificationTypeEnum.SYSTEM),
                pagination = LastIdPaginationRequest(
                    lastId = lastId,
                    pageSize = size
                )
            )
        )
        return ResponseEntity.ok(
            GetNotificationsResponseView(
                notifications = notifications.responseCollection.map { notificationMapper.fromInternal(it) },
                page = lastIdPaginationResponseMapper.fromInternal(notifications)
            )
        )
    }

    override fun notificationsLastGet(type: NotificationTypeEnum?): ResponseEntity<GetLastNotificationsResponseView> {
        val userId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.FORBIDDEN)
        val notification = notificationService.getLastNotification(
            GetUserLastNotificationDto(
                userId = userId,
                notificationType = notificationTypeMapper.mapToInternal(type ?: NotificationTypeEnum.SYSTEM)
            )
        )
        return ResponseEntity.ok(
            GetLastNotificationsResponseView(
                notification = notification?.let { notificationMapper.fromInternal(it) }
            )
        )
    }
}