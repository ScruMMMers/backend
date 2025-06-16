package com.quqee.backend.internship_hits.public_interface.semester_interface

import java.time.OffsetDateTime

data class UpdateSemesterDto(
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val practiceDiaryStart: OffsetDateTime,
    val practiceDiaryEnd: OffsetDateTime,
    val companyChangeStart: OffsetDateTime,
    val companyChangeEnd: OffsetDateTime
)