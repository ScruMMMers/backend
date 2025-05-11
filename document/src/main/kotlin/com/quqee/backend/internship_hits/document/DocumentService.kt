package com.quqee.backend.internship_hits.document

import com.quqee.backend.internship_hits.document.generation.ReportStrategyFactory
import com.quqee.backend.internship_hits.document.model.ReportType
import com.quqee.backend.internship_hits.document.model.StudentSummaryDto
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.public_interface.students_public.GetStudentsListDto
import com.quqee.backend.internship_hits.public_interface.students_public.GetStudentsListFilterParamDto
import com.quqee.backend.internship_hits.public_interface.students_public.StudentDto
import com.quqee.backend.internship_hits.students.StudentsService
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.util.*

@Service
class DocumentService(
    private val studentsService: StudentsService
) {
    private val emptyCompanyName = "Без компании"

    fun generateInternshipReport(companyIds: List<UUID>?): ByteArray {
        val grouped: Map<String, List<StudentSummaryDto>> = getStudentsGroupedByCompany(companyIds)

        val strategy = ReportStrategyFactory
            .getStrategy<StudentSummaryDto>(ReportType.INTERNSHIP)

        return strategy.generate(grouped).toByteArray()
    }

    private fun XSSFWorkbook.toByteArray(): ByteArray {
        val outputStream = ByteArrayOutputStream()
        this.write(outputStream)
        this.close()
        return outputStream.toByteArray()
    }

    private fun getStudentsGroupedByCompany(companyIds: List<UUID>?): Map<String, List<StudentSummaryDto>> {
        val allStudents = getStudentList(companyIds)

        return allStudents
            .groupBy(
                keySelector = { it.company?.name ?: emptyCompanyName },
                valueTransform = {
                    StudentSummaryDto(
                        name = it.profile.fullName,
                        course = it.course,
                        group = it.group
                    )
                }
            )
    }

    private fun getStudentList(companyIds: List<UUID>?): List<StudentDto> {
        val allStudents = mutableListOf<StudentDto>()
        var lastId: UserId? = null

        do {
            val request = GetStudentsListDto(
                pagination = LastIdPaginationRequest(
                    lastId = lastId,
                    pageSize = 100
                ),
                filter = GetStudentsListFilterParamDto(
                    course = setOf(3, 4),
                    group = null,
                    logType = null,
                    logApprovalStatus = null,
                    positionType = null,
                    positionName = null
                )
            )

            val response = studentsService.getStudentsList(request)
            val filteredStudents = response.responseCollection.filter {
                companyIds.isNullOrEmpty() || it.company?.companyId in companyIds
            }

            allStudents.addAll(filteredStudents)

            lastId = response.lastId

        } while (response.responseCollection.isNotEmpty())

        return allStudents
    }
}
