package com.quqee.backend.internship_hits.api.mapper

import com.quqee.backend.internship_hits.mapper.EnumerationMapper
import com.quqee.backend.internship_hits.mapper.makeEnumerationMapper
import com.quqee.backend.internship_hits.model.rest.ApprovalStatusEnum
import com.quqee.backend.internship_hits.model.rest.LogTypeEnum
import com.quqee.backend.internship_hits.model.rest.NotificationTypeEnum
import com.quqee.backend.internship_hits.model.rest.RoleEnum
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class EnumerationMapperConfiguration {
    @Bean
    open fun notificationTypeMapper(): EnumerationMapper<NotificationTypeEnum, NotificationType> {
        return makeEnumerationMapper(NotificationTypeEnum::class, NotificationType::class)
    }

    @Bean
    open fun userRoleMapper(): EnumerationMapper<RoleEnum, UserRole> {
        return makeEnumerationMapper(RoleEnum::class, UserRole::class)
    }

    @Bean
    fun mapLogType(): EnumerationMapper<LogTypeEnum, LogType> {
        return makeEnumerationMapper(LogTypeEnum::class, LogType::class)
    }

    @Bean
    fun mapApprovalStatus(): EnumerationMapper<ApprovalStatusEnum, ApprovalStatus> {
        return makeEnumerationMapper(ApprovalStatusEnum::class, ApprovalStatus::class)
    }
}