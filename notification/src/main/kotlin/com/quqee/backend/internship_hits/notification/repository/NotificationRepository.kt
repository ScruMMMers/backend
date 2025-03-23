package com.quqee.backend.internship_hits.notification.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.quqee.backend.internship_hits.notification.dto.CreateNotificationDtoInternal
import com.quqee.backend.internship_hits.notification.entity.NotificationEntity
import com.quqee.backend.internship_hits.notification.entity.mapper.NotificationEntityMapper
import com.quqee.backend.internship_hits.notification.public.tables.references.NOTIFICATION
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.SortingStrategy
import com.quqee.backend.internship_hits.public_interface.common.exception.NotificationId
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.JSONB
import org.jooq.SortField
import org.springframework.stereotype.Repository

@Repository
class NotificationRepository(
    private val dsl: DSLContext,
) {
    fun createNotifications(notifications: Collection<CreateNotificationDtoInternal>) {
        dsl.batch(
            notifications.map {
                dsl.insertInto(NOTIFICATION)
                    .set(NOTIFICATION.TITLE, it.title)
                    .set(NOTIFICATION.MESSAGE, it.message)
                    .set(NOTIFICATION.CREATED_AT, it.createdAt)
                    .set(NOTIFICATION.IS_READ, it.isRead)
                    .set(NOTIFICATION.RECEIVER_ID, it.userId)
                    .set(NOTIFICATION.TYPE, it.type.toDataBase())
                    .set(NOTIFICATION.CHANNELS, it.channels.map { channel ->
                        channel.toDataBase()
                    }.toTypedArray())
                    .set(NOTIFICATION.ATTACHMENT, it.attachment?.let { attachment ->
                        JSONB.valueOf(jsonMapper.writeValueAsString(attachment))
                    })
            }
        ).execute()
    }

    fun getNotifications(
        pagination: LastIdPaginationRequest<NotificationId>,
        filter: NotificationFilterParams,
    ): Collection<NotificationEntity> {
        return if (pagination.lastId != null) {
            dsl.selectFrom(NOTIFICATION)
                .where(filter.toConditions())
                .orderBy(pagination.sorting.toOrderBy())
                .seek(pagination.lastId)
                .limit(pagination.sizeForSelect)
                .fetch(notificationMapper)
        } else {
            dsl.selectFrom(NOTIFICATION)
                .where(filter.toConditions())
                .orderBy(pagination.sorting.toOrderBy())
                .limit(pagination.sizeForSelect)
                .fetch(notificationMapper)
        }
    }

    private fun NotificationFilterParams.toConditions(): List<Condition> {
        val conditions = mutableListOf<Condition>()
        ids?.let { conditions.add(NOTIFICATION.ID.`in`(it)) }
        type?.let { conditions.add(NOTIFICATION.TYPE.eq(it.toDataBase())) }
        userId?.let { conditions.add(NOTIFICATION.RECEIVER_ID.eq(it)) }
        isRead?.let { conditions.add(NOTIFICATION.IS_READ.eq(it)) }
        return conditions
    }

    private fun SortingStrategy.toOrderBy(): List<SortField<*>> {
        return when (this) {
            SortingStrategy.CREATED_AT_ASC -> listOf(NOTIFICATION.CREATED_AT.asc())
            SortingStrategy.CREATED_AT_DESC -> listOf(NOTIFICATION.CREATED_AT.desc())
        }
    }

    companion object {
        private val notificationMapper = NotificationEntityMapper()
        private val jsonMapper = jacksonObjectMapper()
    }
}