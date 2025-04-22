package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.notification.service.NotificationService
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.notification_public.GetNotificationForHeaderDto
import com.quqee.backend.internship_hits.public_interface.notification_public.ShortNotificationDto
import com.quqee.backend.internship_hits.public_interface.profile_public.GetProfileDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.net.URI
import java.util.*

@Component
class ProfileController(
    private val profileService: ProfileService,
    private val notificationService: NotificationService,
    private val shortNotificationMapper: FromInternalToApiMapper<ShortNotificationView, ShortNotificationDto>,
) : ProfileApiDelegate {
    override fun profileHeaderGet(): ResponseEntity<GetProfileHeaderResponseView> {
        val userId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.FORBIDDEN)
        val headerProfile = profileService.getProfileForHeader(GetProfileDto(userId))
        val notifications = notificationService.getNotificationsForHeader(GetNotificationForHeaderDto(userId))

        return ResponseEntity.ok(
            GetProfileHeaderResponseView(
                fullName = headerProfile.fullName,
                avatarUrl = URI.create(headerProfile.avatarUrl),
                notifications = notifications.map { shortNotificationMapper.fromInternal(it) }
            )
        )
    }

    override fun profileMeGet(): ResponseEntity<GetMyProfileResponseView> {
        return ResponseEntity.ok(
            GetMyProfileResponseView(
                profile = ProfileView(
                    fullName = "Строго",
                    avatarUrl = URI.create("https://cs-config.ru/_ld/35/72750554.png"),
                    roles = listOf(
                        RoleEnum.STUDENT_SECOND
                    ),
                    course = "1 course",
                    group = "1 group",
                    shortCompany = ShortCompanyView(
                        companyId = UUID.randomUUID(),
                        name = "Яндекс",
                        avatarUrl = URI.create("https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Yandex_icon.svg/2048px-Yandex_icon.svg.png"),
                        primaryColor = ColorEnum.NAVY.hexColor,
                        sinceYear = "2000"
                    )
                )
            )
        )
    }
}