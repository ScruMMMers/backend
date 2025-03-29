package com.quqee.backend.internship_hits.public_interface.logs

import com.quqee.backend.internship_hits.public_interface.enums.LogType
import java.util.*

data class UpdateLogRequestDto(
    val message: String,
    val type: LogType,
    val files: List<UUID>?
)
