package com.quqee.backend.internship_hits.public_interface.employees_public

import java.util.UUID

data class CreateEmployeeDto(
    val fullName: String,
    val email: String,
    val photoId: String?,
    val companyIds: List<UUID>,
)
