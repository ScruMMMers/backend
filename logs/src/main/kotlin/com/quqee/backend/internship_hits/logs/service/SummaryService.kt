package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.logs.repository.LogsRepository
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import org.apache.kafka.shaded.io.opentelemetry.proto.metrics.v1.Summary
import java.util.*

interface SummaryService {
    fun getAllPracticeDiaryForCurrentSemester(): List<Summary>
    fun getAllPracticeDiaryBySemester(semester: Int): List<Summary>
}

open class SummaryServiceImpl(
    private val logsRepository: LogsRepository,
) : SummaryService {


    private fun getCurrentSemester(course: Int): Int {
        val currentDate = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply { timeInMillis = currentDate }

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

    private fun getCurrentUser(): UUID {
        return KeycloakUtils.getUserId() ?: throw ExceptionInApplication(ExceptionType.FORBIDDEN)
    }
}