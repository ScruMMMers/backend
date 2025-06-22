package com.quqee.backend.internship_hits.students.dtos

import com.quqee.backend.internship_hits.public_interface.common.UserId

data class StudentFromFileDto(
    val userId: UserId,
    val password: String,
    val login: String,
)
