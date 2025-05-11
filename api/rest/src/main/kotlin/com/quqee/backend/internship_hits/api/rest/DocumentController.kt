package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.document.DocumentService
import com.quqee.backend.internship_hits.logs.service.LogsSummaryService
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Component
class DocumentController(
    private val documentService: DocumentService,
    private val logsSummaryService: LogsSummaryService
) : DocumentApiDelegate {

    override fun documentsInternshipCurrentGet(companyIds: List<UUID>?): ResponseEntity<Resource> {
        val reportBytes = documentService.generateInternshipReport(companyIds)

        val filename = "internship_report_${getCurrentDate()}.xlsx"
        return createExcelResponse(reportBytes, filename)
    }

    override fun documentsInternshipArchiveGet(course: Int): ResponseEntity<Resource> {
        val zipBytes = logsSummaryService.getAllPracticeDiaryForCurrentSemester(course)
        val filename = "internship_diary_${getCurrentDate()}.zip"
        return createZipResponse(zipBytes, filename)
    }

    private fun createExcelResponse(bytes: ByteArray, filename: String): ResponseEntity<Resource> {
        val resource = ByteArrayResource(bytes)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
            .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .contentLength(bytes.size.toLong())
            .body(resource)
    }

    private fun createZipResponse(bytes: ByteArray, filename: String): ResponseEntity<Resource> {
        val resource = ByteArrayResource(bytes)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
            .header(HttpHeaders.CONTENT_TYPE, "application/zip")
            .contentLength(bytes.size.toLong())
            .body(resource)
    }

    private fun getCurrentDate(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }
}