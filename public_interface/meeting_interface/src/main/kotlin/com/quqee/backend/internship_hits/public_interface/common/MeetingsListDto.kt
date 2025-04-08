package com.quqee.backend.internship_hits.public_interface.common

data class MeetingsListDto (
    val meetings: List<MeetingDto>,
    val page: LastIdPagination
)