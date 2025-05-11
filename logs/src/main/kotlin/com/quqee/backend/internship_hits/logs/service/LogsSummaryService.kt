package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.file.service.FileService
import com.quqee.backend.internship_hits.logs.repository.LogsRepository
import com.quqee.backend.internship_hits.logs.utils.SemesterUtils.getCurrentSemester
import com.quqee.backend.internship_hits.logs.utils.SemesterUtils.getDatesBySemester
import org.apache.kafka.shaded.io.opentelemetry.proto.metrics.v1.Summary
import org.springframework.stereotype.Service

interface LogsSummaryService {
    fun getAllPracticeDiaryForCurrentSemester(course: Int): ByteArray
}

@Service
open class LogsSummaryServiceImpl(
    private val logsRepository: LogsRepository,
    private val fileService: FileService
) : LogsSummaryService {

    override fun getAllPracticeDiaryForCurrentSemester(course: Int): ByteArray {
        val semester = getCurrentSemester(course)
        val dates = getDatesBySemester(semester)
        val logs = logsRepository.getApprovedPracticeDiariesByPeriod(dates.first, dates.second)
        val fileIds = logs.flatMap { it.fileIds }.distinct()
        return fileService.getZipByIds(fileIds)
    }
}