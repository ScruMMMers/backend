package com.quqee.backend.internship_hits.public_interface.common

data class StudentsMarksListDto(
    val students: List<StudentsMarksDto>,
    val page: LastIdPagination
)