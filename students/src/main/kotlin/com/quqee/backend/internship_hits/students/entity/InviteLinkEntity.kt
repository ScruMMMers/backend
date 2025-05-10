package com.quqee.backend.internship_hits.students.entity

import com.quqee.backend.internship_hits.notification.public.tables.InviteLink.Companion.INVITE_LINK
import com.quqee.backend.internship_hits.public_interface.common.IActionParams
import java.time.OffsetDateTime
import java.util.UUID

data class InviteLinkEntity(
    val id: UUID,
    val config: InviteLinkConfigEntity,
    val createdAt: OffsetDateTime,
) {
    companion object {
        val FIELDS = listOf(
            INVITE_LINK.LINK_ID,
            INVITE_LINK.CONFIG,
            INVITE_LINK.CREATED_AT,
        )
    }
}

data class InviteLinkConfigEntity(
    val course: Int,
    val group: String?,
) : IActionParams
