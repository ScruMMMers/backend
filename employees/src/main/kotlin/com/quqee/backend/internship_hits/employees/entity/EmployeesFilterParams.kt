package com.quqee.backend.internship_hits.employees.entity

import com.quqee.backend.internship_hits.public_interface.common.UserId
import java.util.UUID

data class EmployeesFilterParams(
    val companiesIds: List<UUID>?,
    val employeesIds: List<UserId>?,
)
