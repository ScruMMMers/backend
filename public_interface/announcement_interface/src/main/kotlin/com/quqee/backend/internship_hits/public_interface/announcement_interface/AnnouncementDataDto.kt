package com.quqee.backend.internship_hits.public_interface.announcement_interface

import java.util.*

data class AnnouncementDataDto(
    val title: String,
    val text: String,
    val pollId: UUID? = null,
    val redirectId: UUID? = null
)
