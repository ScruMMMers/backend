package com.quqee.backend.internship_hits.api.rest.config.mapper

import com.quqee.backend.internship_hits.mapper.EnumerationMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.NotificationTypeEnum
import com.quqee.backend.internship_hits.model.rest.ShortNotificationView
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import com.quqee.backend.internship_hits.public_interface.notification_public.ShortNotificationDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class NotificationMapper(
    private val notificationTypeMapper: EnumerationMapper<NotificationTypeEnum, NotificationType>,
) {
    @Bean
    open fun shortNotificationMapper(): FromInternalToApiMapper<ShortNotificationView, ShortNotificationDto> = makeToApiMapper { model ->
        ShortNotificationView(
            notificationId = model.notificationId,
            title = model.title,
            notificationType = notificationTypeMapper.mapToApi(model.type),
            description = model.description,
            createdAt = model.createdAt,
            isRead = model.isRead,
        )
    }
}