package com.quqee.backend.internship_hits.students.public_interface

data class CreateStudentsFromFileDto(
    val email: String,
    val firstName: String,
    val lastName: String,
    val course: Int,
    val group: String,
    val middleName: String?,
)
