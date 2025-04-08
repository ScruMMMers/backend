package com.quqee.backend.internship_hits.meeting.entity

import com.quqee.backend.internship_hits.public_interface.common.enums.MeetingTypeEnum
import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*


/**
 * Сущность встречи
 */
@Entity
@Table(name = "meeting")
data class MeetingEntity(

    @Id
    @Column(name = "id", nullable = false)
    var id: UUID,

    @Column(name = "date", nullable = false)
    var date: OffsetDateTime,

    @Column(name = "place", nullable = true)
    var place: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "meeting_type", nullable = false)
    var meetingType: MeetingTypeEnum,

    @Column(name = "company_id", nullable = false)
    var companyId: UUID,

    ) {
    constructor() : this(
        UUID.randomUUID(),
        OffsetDateTime.now(),
        "",
        MeetingTypeEnum.ONLINE,
        UUID.randomUUID(),
    )
}