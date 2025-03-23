package com.quqee.backend.internship_hits.api.mapper.notification

import com.quqee.backend.internship_hits.mapper.EnumerationMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.NotificationTypeEnum
import com.quqee.backend.internship_hits.model.rest.NotificationView
import com.quqee.backend.internship_hits.model.rest.ShortNotificationView
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationDto
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import com.quqee.backend.internship_hits.public_interface.notification_public.ShortNotificationDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class NotificationMapperConfiguration(
    private val notificationTypeMapper: EnumerationMapper<NotificationTypeEnum, NotificationType>,
) {
    @Bean
    open fun shortNotificationMapper(): FromInternalToApiMapper<ShortNotificationView, ShortNotificationDto> = makeToApiMapper { model ->
        ShortNotificationView(
            notificationId = model.id,
            title = model.title,
            notificationType = notificationTypeMapper.mapToApi(model.type),
            description = model.description,
            createdAt = model.createdAt,
            isRead = model.isRead,
        )
    }

    @Bean
    open fun notificationMapper(): FromInternalToApiMapper<NotificationView, NotificationDto> = makeToApiMapper { model ->
        NotificationView(
            notificationId = model.id,
            title = model.title,
            notificationType = notificationTypeMapper.mapToApi(model.type),
            description = model.description,
            createdAt = model.createdAt,
            isRead = model.isRead,
            pollId = model.pollId,
        )
    }
}