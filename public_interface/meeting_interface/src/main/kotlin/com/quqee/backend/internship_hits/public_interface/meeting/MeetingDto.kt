package com.quqee.backend.internship_hits.public_interface.meeting

import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.meeting.enums.MeetingTypeEnum
import java.time.OffsetDateTime
import java.util.UUID

data class MeetingDto(
    val id: UUID,
    val date: OffsetDateTime,
    val place: String?,
    val meetingType: MeetingTypeEnum,
    val company: ShortCompanyDto,
)
