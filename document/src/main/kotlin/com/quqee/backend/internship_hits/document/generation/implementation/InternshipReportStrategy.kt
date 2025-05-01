package com.quqee.backend.internship_hits.document.generation.implementation

import com.quqee.backend.internship_hits.document.generation.DocumentGenerationStrategy
import com.quqee.backend.internship_hits.document.model.StudentSummaryDto
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class InternshipReportStrategy : DocumentGenerationStrategy<StudentSummaryDto> {
    override fun generate(params: Map<String, List<StudentSummaryDto>>): XSSFWorkbook {
        val workbook = XSSFWorkbook()

        params.forEach { (companyName, students) ->
            val sheet = workbook.createSheet(companyName.take(31))

            val groupedByCourse = students.groupBy { it.course }

            var rowIndex = 0
            groupedByCourse.forEach { (course, courseStudents) ->
                val courseRow = sheet.createRow(rowIndex++)
                courseRow.createCell(0).setCellValue("$course курс")

                val headerRow = sheet.createRow(rowIndex++)
                headerRow.createCell(0).setCellValue("ФИО")
                headerRow.createCell(1).setCellValue("Курс")
                headerRow.createCell(2).setCellValue("Группа")

                courseStudents.forEach { student ->
                    val row = sheet.createRow(rowIndex++)
                    row.createCell(0).setCellValue(student.name)
                    row.createCell(1).setCellValue(student.course.toString())
                    row.createCell(2).setCellValue(student.group)
                }

                rowIndex++
            }

            (0..2).forEach { sheet.autoSizeColumn(it) }
        }

        return workbook
    }
}