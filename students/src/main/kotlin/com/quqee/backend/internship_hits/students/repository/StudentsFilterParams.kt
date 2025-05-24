package com.quqee.backend.internship_hits.students.repository

import com.quqee.backend.internship_hits.public_interface.common.enums.Position
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import java.util.UUID

data class StudentsFilterParams(
    val course: Set<Int>? = null,
    val group: Set<String>? = null,
    val logType: Set<LogType>? = null,
    val logApprovalStatus: Set<ApprovalStatus>? = null,
    val positionType: Set<Position>? = null,
    val positionName: Set<String>? = null,
    val companyIds: Set<UUID>? = null,
    val logByCompany: Map<LogType, Set<UUID>>? = null,
    val userIds: Set<UUID>? = null,
)
