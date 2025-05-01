package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.document.DocumentService
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class DocumentController(
    private val documentService: DocumentService
) : DocumentApiDelegate {

    override fun documentsInternshipCurrentGet(): ResponseEntity<Resource> {
        val reportBytes = documentService.generateInternshipReport()

        val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val filename = "internship_report_$currentDate.xlsx"
        return createExcelResponse(reportBytes, filename)
    }

    private fun createExcelResponse(bytes: ByteArray, filename: String): ResponseEntity<Resource> {
        val resource = ByteArrayResource(bytes)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
            .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            .contentLength(bytes.size.toLong())
            .body(resource)
    }
}