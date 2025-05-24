package com.quqee.backend.internship_hits.public_interface.employees_public

import java.util.*

data class GetEmployeesListFilterParamDto(
    val companiesIds: List<UUID>?,
    val name: String?,
)
