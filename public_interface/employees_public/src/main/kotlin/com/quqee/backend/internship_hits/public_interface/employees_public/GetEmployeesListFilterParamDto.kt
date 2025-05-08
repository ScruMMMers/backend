package com.quqee.backend.internship_hits.public_interface.employees_public

import com.quqee.backend.internship_hits.public_interface.common.UserId
import java.util.UUID

data class GetEmployeesListFilterParamDto(
    val companiesIds: List<UUID>?,
    val employeesIds: List<UserId>?,
)
