package com.quqee.backend.internship_hits.public_interface.logs

import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import java.util.UUID

data class ShortLogInfo(
    val id: UUID,
    val type: LogType,
    val status: ApprovalStatus,
)
