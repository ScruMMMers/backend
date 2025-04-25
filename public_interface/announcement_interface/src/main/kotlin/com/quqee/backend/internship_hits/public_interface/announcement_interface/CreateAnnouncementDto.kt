package com.quqee.backend.internship_hits.public_interface.announcement_interface

import com.quqee.backend.internship_hits.public_interface.common.UserId

data class CreateAnnouncementDto(
    val data: AnnouncementDataDto,
    val userIds: List<UserId>
)
