package com.quqee.backend.internship_hits.public_interface.semester_interface

import java.util.*

data class AcademicYearDto(
    val id: UUID,
    val yearRange: String,
    val semesters: List<SemesterDto>
)