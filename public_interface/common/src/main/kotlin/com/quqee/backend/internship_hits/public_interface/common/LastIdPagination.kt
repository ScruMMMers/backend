package com.quqee.backend.internship_hits.public_interface.common

data class LastIdPagination(
    val lastId: String?,
    val pageSize: Int,
    val hasNext: Boolean
)
