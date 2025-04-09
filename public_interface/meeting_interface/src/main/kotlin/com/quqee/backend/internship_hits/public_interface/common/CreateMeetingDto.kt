package com.quqee.backend.internship_hits.public_interface.common

import com.quqee.backend.internship_hits.public_interface.common.enums.MeetingTypeEnum
import java.time.OffsetDateTime

data class CreateMeetingDto(
    val date: OffsetDateTime,
    val place: String?,
    val meetingType: MeetingTypeEnum
)
