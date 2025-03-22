package com.quqee.backend.internship_hits.public_interface.common

import java.util.UUID

data class LastIdPagination(
    val lastId: UUID?,
    val pageSize: Int,
    val hasNext: Boolean
)
