package com.quqee.backend.internship_hits.notification.entity.mapper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.quqee.backend.internship_hits.notification.entity.NotificationAttachmentEntity
import com.quqee.backend.internship_hits.notification.entity.NotificationEntity
import com.quqee.backend.internship_hits.notification.public.tables.records.NotificationRecord
import com.quqee.backend.internship_hits.notification.public.tables.references.NOTIFICATION
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import org.jooq.RecordMapper

class NotificationEntityMapper : RecordMapper<NotificationRecord, NotificationEntity> {
    override fun map(record: NotificationRecord): NotificationEntity {
        return NotificationEntity(
            id = record[NOTIFICATION.ID]!!,
            title = record[NOTIFICATION.TITLE]!!,
            message = record[NOTIFICATION.MESSAGE]!!,
            createdAt = record[NOTIFICATION.CREATED_AT]!!,
            isRead = record[NOTIFICATION.IS_READ]!!,
            userId = record[NOTIFICATION.RECEIVER_ID]!!,
            type = NotificationType.fromDataBase(record[NOTIFICATION.TYPE]!!),
            channels = record[NOTIFICATION.CHANNELS]!!.map { NotificationChannel.fromDataBase(it!!) }.toSet(),
            attachment = record[NOTIFICATION.ATTACHMENT]?.let { MAPPER.readValue(it.data(), NotificationAttachmentEntity::class.java) },
        )
    }

    companion object {
        private val MAPPER = jacksonObjectMapper()
    }
}