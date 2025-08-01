package com.quqee.backend.internship_hits.notification.service

import com.quqee.backend.internship_hits.notification.dto.CreateNotificationDtoInternal
import com.quqee.backend.internship_hits.notification.entity.NotificationAttachmentEntity
import com.quqee.backend.internship_hits.notification.entity.NotificationEntity
import com.quqee.backend.internship_hits.notification.repository.NotificationFilterParams
import com.quqee.backend.internship_hits.notification.repository.NotificationRepository
import com.quqee.backend.internship_hits.notification.service.channel.ChannelHandler
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.*
import com.quqee.backend.internship_hits.public_interface.notification_public.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.OffsetDateTime

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val clock: Clock,
    private val channelHandlers: List<ChannelHandler>,
    private val profileService: ProfileService
) {
    private val channelHandlesByType = channelHandlers.associateBy { it.channelType }

    @Transactional
    fun createNotifications(dtos: List<CreateNotificationDto>) {
        val internalNotifications = dtos.map {
            val attachment = NotificationAttachmentEntity(
                pollId = it.pollId,
                redirectId = it.redirectId
            )
            CreateNotificationDtoInternal(
                title = it.title,
                message = it.message,
                createdAt = OffsetDateTime.now(clock),
                isRead = false,
                userId = it.userId,
                type = it.type,
                channels = it.channels,
                attachment = attachment,
            )
        }
        notificationRepository.createNotifications(internalNotifications)
    }

    fun getNotificationsForHeader(dto: GetNotificationForHeaderDto): List<ShortNotificationDto> {
        return notificationRepository.getNotifications(
            LastIdPaginationRequest(
                pageSize = 3,
                sorting = SortingStrategy.CREATED_AT_DESC
            ),
            NotificationFilterParams(
                userId = dto.user
            )
        ).map { mapShortNotificationToDto(it) }
    }

    fun getLastNotification(dto: GetUserLastNotificationDto): NotificationDto? {
        val notifications = notificationRepository.getNotifications(
            LastIdPaginationRequest(
                pageSize = 1,
                sorting = SortingStrategy.CREATED_AT_DESC
            ),
            NotificationFilterParams(
                userId = dto.userId,
                type = dto.notificationType,
            )
        )

        return notifications.firstOrNull()?.let {
            mapNotificationToDto(it)
        }
    }

    fun getNotifications(dto: GetUserNotificationsDto): LastIdPaginationResponse<NotificationDto, NotificationId> {
        val notifications = runBlocking {
            notificationRepository.getNotifications(
                dto.pagination,
                NotificationFilterParams(
                    userId = dto.userId,
                    type = dto.notificationType,
                )
            ).map {
                async {
                    mapNotificationToDto(it)
                }
            }.awaitAll()
        }

        return LastIdPaginationResponse(
            notifications,
            dto.pagination,
            null
        )
    }

    fun getShortNotifications(dto: GetUserNotificationsDto): LastIdPaginationResponse<ShortNotificationDto, NotificationId> {
        val notifications = notificationRepository.getNotifications(
            dto.pagination,
            NotificationFilterParams(
                userId = dto.userId,
                type = dto.notificationType,
            )
        ).map {
            mapShortNotificationToDto(it)
        }

        return LastIdPaginationResponse(
            notifications,
            dto.pagination,
            null,
        )
    }

    fun sendNotifications(dtos: List<SendNotificationDto>) {
        runBlocking {
            val deferred = dtos.map { dto ->
                async {
                    dto.channels.map { channel ->
                        val channelHandler = channelHandlesByType[channel]
                        channelHandler?.handle(dto)
                    }
                }
            }
            deferred.forEach { it.await() }
        }
    }

    fun markReadNotifications(dto: ReadNotificationsDto) {
        val userNotificationIds = notificationRepository.getUserNotificationIds(dto.userId, dto.notificationIds)

        val validNotificationIds = dto.notificationIds.filter { it in userNotificationIds }

        if (validNotificationIds.isNotEmpty()) {
            notificationRepository.markNotificationsAsRead(validNotificationIds)
        }
    }

    private fun mapNotificationToDto(notification: NotificationEntity): NotificationDto {
        return NotificationDto(
            id = notification.id,
            title = notification.title,
            type = notification.type,
            description = notification.message,
            createdAt = notification.createdAt,
            isRead = notification.isRead,
            pollId = notification.attachment?.pollId,
            redirect = notification.attachment?.redirectId?.let { redirectId ->
                val profile = profileService.getProfileForHeader(GetProfileDto(redirectId))
                profile.let {
                    RedirectNotificationDto(
                        redirectId = redirectId,
                        fullName = it.fullName,
                        avatarUrl = it.avatarUrl
                    )
                }
            }
        )
    }

    private fun mapShortNotificationToDto(notification: NotificationEntity): ShortNotificationDto {
        return ShortNotificationDto(
            id = notification.id,
            title = notification.title,
            type = notification.type,
            description = notification.message,
            createdAt = notification.createdAt,
            isRead = notification.isRead,
        )
    }
}