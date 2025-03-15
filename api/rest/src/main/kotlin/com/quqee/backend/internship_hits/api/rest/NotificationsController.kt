package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.model.rest.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.UUID

@Component
class NotificationsController : NotificationsApiDelegate {
    override fun notificationsGet(
        type: NotificationTypeEnum?,
        lastId: Int?,
        size: Int?
    ): ResponseEntity<GetNotificationsResponseView> {
        return ResponseEntity.ok(
            GetNotificationsResponseView(
                notifications = listOf(
                    NotificationView(
                        notificationId = UUID.randomUUID(),
                        title = "Заголовок",
                        notificationType = NotificationTypeEnum.DEANERY,
                        description = "Описание",
                        createdAt = OffsetDateTime.now(),
                        isRead = false,
                    )
                ),
                page = LastIdPaginationView(
                    lastId = UUID.randomUUID().toString(),
                    pageSize = 1,
                    hasNext = false,
                )
            )
        )
    }

    override fun notificationsLastGet(type: NotificationTypeEnum?): ResponseEntity<GetLastNotificationsResponseView> {
        return ResponseEntity.ok(
            GetLastNotificationsResponseView(
                NotificationView(
                    notificationId = UUID.randomUUID(),
                    title = "Заголовок",
                    notificationType = NotificationTypeEnum.DEANERY,
                    description = "Описание",
                    createdAt = OffsetDateTime.now(),
                    isRead = false,
                )
            )
        )
    }
}