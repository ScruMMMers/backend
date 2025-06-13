package com.quqee.backend.internship_hits.public_interface.meeting

import com.quqee.backend.internship_hits.public_interface.meeting.enums.MeetingTypeEnum
import java.time.OffsetDateTime

data class CreateMeetingDto(
    val date: OffsetDateTime,
    val place: String?,
    val meetingType: MeetingTypeEnum
)
