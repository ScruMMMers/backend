package com.quqee.backend.internship_hits.public_interface.common

import java.util.UUID

data class StudentsMarksDto(
    val id: UUID,
    val fullName: String,
    val group: String,
    val course: Int,
    val marks: List<MarkDto>
)