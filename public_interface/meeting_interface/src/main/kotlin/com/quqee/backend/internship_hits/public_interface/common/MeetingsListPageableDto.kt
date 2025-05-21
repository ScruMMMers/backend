package com.quqee.backend.internship_hits.public_interface.common

data class MeetingsListPageableDto (
    val meetings: List<MeetingDto>,
    val page: LastIdPagination
)