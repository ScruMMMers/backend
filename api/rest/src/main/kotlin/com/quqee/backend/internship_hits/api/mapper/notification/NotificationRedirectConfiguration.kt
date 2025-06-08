package com.quqee.backend.internship_hits.api.mapper.notification

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.RedirectNotificationView
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.public_interface.notification_public.RedirectNotificationDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NotificationRedirectConfiguration {
    @Bean
    fun notificationRedirectMapper(): FromInternalToApiMapper<RedirectNotificationView, RedirectNotificationDto> = makeToApiMapper { model ->
        RedirectNotificationView(
            redirectId = model.redirectId,
            fullName = model.fullName,
            avatarUrl = model.avatarUrl
        )
    }
}