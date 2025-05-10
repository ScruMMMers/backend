package com.quqee.backend.internship_hits.students.entity

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.quqee.backend.internship_hits.notification.public.tables.records.InviteLinkRecord
import com.quqee.backend.internship_hits.notification.public.tables.references.INVITE_LINK
import org.jooq.RecordMapper

class InviteLinkEntityMapper : RecordMapper<InviteLinkRecord, InviteLinkEntity> {
    override fun map(record: InviteLinkRecord): InviteLinkEntity {
        return InviteLinkEntity(
            id = record[INVITE_LINK.LINK_ID]!!,
            config = OBJECT_MAPPER.readValue(record[INVITE_LINK.CONFIG]!!.data(), InviteLinkConfigEntity::class.java),
            createdAt = record[INVITE_LINK.CREATED_AT]!!,
        )
    }

    companion object {
        val OBJECT_MAPPER = jacksonObjectMapper()
    }
}