package com.quqee.backend.internship_hits.public_interface.message.logs

import com.quqee.backend.internship_hits.public_interface.enums.LogType
import java.util.UUID

data class NewLogDto(
    val userId: UUID,
    val logType: LogType
)
