package com.quqee.backend.internship_hits.public_interface.employees_public

data class CreateEmployeeDto(
    val fullName: String,
    val email: String,
    val photoId: String?
)
