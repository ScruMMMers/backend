package com.quqee.backend.internship_hits.public_interface.students_public

import com.quqee.backend.internship_hits.public_interface.common.enums.Position
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import java.util.UUID

data class GetStudentsListFilterParamDto(
    val course: Set<Int>? = null,
    val group: Set<String>? = null,
    val logType: Set<LogType>? = null,
    val logApprovalStatus: Set<ApprovalStatus>? = null,
    val positionType: Set<Position>? = null,
    val positionName: Set<String>? = null,
    val companyIds: Set<UUID>? = null,
    val logByCompany: Map<LogType, LogByCompanyItem>? = null,
    val name: String? = null,
    val withoutCompanies: Boolean = false,
    val withoutLogs: Boolean = false,
)

data class LogByCompanyItem(
    val companyIds: Set<UUID>? = null,
    val positionTypes: Set<Position>? = null,
    val positionNames: Set<String>? = null,
)
