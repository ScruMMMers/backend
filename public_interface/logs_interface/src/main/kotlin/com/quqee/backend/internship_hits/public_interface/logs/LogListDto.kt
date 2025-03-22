package com.quqee.backend.internship_hits.public_interface.logs

import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination

data class LogListDto(
    val logs: List<LogDto>,
    val page: LastIdPagination
)