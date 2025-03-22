package com.quqee.backend.internship_hits.profile.dto

data class CreateUserDto(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
)
