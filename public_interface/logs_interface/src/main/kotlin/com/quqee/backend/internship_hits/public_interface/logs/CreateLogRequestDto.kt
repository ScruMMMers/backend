package com.quqee.backend.internship_hits.public_interface.logs

import com.quqee.backend.internship_hits.public_interface.enums.LogTypeEnum

data class CreateLogRequestDto(
    val message: String,
    val type: LogTypeEnum
)
