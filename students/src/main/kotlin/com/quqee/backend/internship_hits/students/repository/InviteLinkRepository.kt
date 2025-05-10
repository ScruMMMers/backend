package com.quqee.backend.internship_hits.students.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.quqee.backend.internship_hits.notification.public.tables.references.INVITE_LINK
import com.quqee.backend.internship_hits.students.entity.InviteLinkEntity
import com.quqee.backend.internship_hits.students.entity.InviteLinkEntityMapper
import com.quqee.backend.internship_hits.students.public_interface.CreateInviteLinkDto
import org.jooq.DSLContext
import org.jooq.JSONB
import org.springframework.stereotype.Repository
import java.time.Clock
import java.time.OffsetDateTime
import java.util.UUID

@Repository
class InviteLinkRepository(
    val dsl: DSLContext,
    val clock: Clock,
) {
    fun createLink(dto: CreateInviteLinkDto): InviteLinkEntity {
        return dsl.insertInto(INVITE_LINK)
            .set(INVITE_LINK.LINK_ID, UUID.randomUUID())
            .set(INVITE_LINK.CREATED_AT, OffsetDateTime.now(clock))
            .set(INVITE_LINK.CONFIG, JSONB.valueOf(jsonMapper.writeValueAsString(dto.config)))
            .returning(InviteLinkEntity.FIELDS)
            .fetchOne(INVITE_LINK_MAPPER)!!
    }

    fun getLink(linkId: UUID): InviteLinkEntity? {
        return dsl.selectFrom(INVITE_LINK)
            .where(INVITE_LINK.LINK_ID.eq(linkId))
            .fetchOne(INVITE_LINK_MAPPER)
    }

    companion object {
        private val INVITE_LINK_MAPPER = InviteLinkEntityMapper()
        private val jsonMapper = jacksonObjectMapper()
    }
}
