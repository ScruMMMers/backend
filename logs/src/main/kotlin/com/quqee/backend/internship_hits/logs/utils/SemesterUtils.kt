package com.quqee.backend.internship_hits.logs.utils

import java.time.OffsetDateTime
import java.util.*

object SemesterUtils {

    fun getCurrentSemester(course: Int): Int {
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val isFirstSemester = when (month) {
            in 9..12 -> true
            1 -> true
            2 -> day <= 15
            else -> false
        }

        return if (isFirstSemester) {
            course * 2 - 1
        } else {
            course * 2
        }
    }

    fun getDatesBySemester(semester: Int): Pair<OffsetDateTime, OffsetDateTime> {
        val now = OffsetDateTime.now()
        val year = now.year

        val info = when (semester % 2) {
            1 -> SemesterDateInfo(9, 1, 2, 15, 0, 1)   // осенний
            else -> SemesterDateInfo(2, 16, 8, 31, 1, 0) // весенний
        }

        val startDate = OffsetDateTime.of(
            year - info.startYearOffset, info.startMonth, info.startDay, 0, 0, 0, 0, now.offset
        )
        val endDate = OffsetDateTime.of(
            year + info.endYearOffset, info.endMonth, info.endDay, 23, 59, 59, 999_999_999, now.offset
        )

        return startDate to endDate
    }

    private data class SemesterDateInfo(
        val startMonth: Int,
        val startDay: Int,
        val endMonth: Int,
        val endDay: Int,
        val startYearOffset: Int,
        val endYearOffset: Int
    )
}
