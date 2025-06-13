package com.quqee.backend.internship_hits.public_interface.meeting

import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination

data class MeetingsListPageableDto (
    val meetings: List<MeetingWithAgentDto>,
    val page: LastIdPagination
)