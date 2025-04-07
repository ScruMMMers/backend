package com.quqee.backend.internship_hits.public_interface.common

import com.quqee.backend.internship_hits.public_interface.common.enums.PositionEnum

data class PositionDto(
    val id: Long,
    val name: String,
    val position: PositionEnum
)
