package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.model.rest.GetLastNotificationsResponseView
import com.quqee.backend.internship_hits.model.rest.GetNotificationsResponseView
import com.quqee.backend.internship_hits.model.rest.NotificationTypeEnum
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class NotificationsController : NotificationsApiDelegate {
    override fun notificationsGet(
        type: NotificationTypeEnum?,
        lastId: Int?,
        size: Int?
    ): ResponseEntity<GetNotificationsResponseView> {
        return super.notificationsGet(type, lastId, size)
    }

    override fun notificationsLastGet(type: NotificationTypeEnum?): ResponseEntity<GetLastNotificationsResponseView> {
        return super.notificationsLastGet(type)
    }
}