package com.quqee.backend.internship_hits.api.rest.config.mapper

import com.quqee.backend.internship_hits.mapper.EnumerationMapper
import com.quqee.backend.internship_hits.mapper.makeEnumerationMapper
import com.quqee.backend.internship_hits.model.rest.NotificationTypeEnum
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class EnumerationMapperConfiguration {
    @Bean
    open fun notificationTypeMapper(): EnumerationMapper<NotificationTypeEnum, NotificationType> {
        return makeEnumerationMapper(NotificationTypeEnum::class, NotificationType::class)
    }
}