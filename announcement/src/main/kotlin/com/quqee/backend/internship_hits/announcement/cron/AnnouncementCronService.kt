package com.quqee.backend.internship_hits.announcement.cron

import com.quqee.backend.internship_hits.announcement.AnnouncementService
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.public_interface.students_public.GetStudentsListDto
import com.quqee.backend.internship_hits.public_interface.students_public.GetStudentsListFilterParamDto
import com.quqee.backend.internship_hits.public_interface.students_public.StudentDto
import com.quqee.backend.internship_hits.students.StudentsService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class AnnouncementCronService(
    private val studentsService: StudentsService,
    private val announcementService: AnnouncementService
) {

    @Scheduled(cron = "0 0 12 * * *")
    fun notifyAllStudents(){
        val students = getStudentList()

        students.forEach { student ->

        }
    }

    private fun getStudentList(): List<StudentDto> {
        val allStudents = mutableListOf<StudentDto>()
        var lastId: UserId? = null

        do {
            val request = GetStudentsListDto(
                pagination = LastIdPaginationRequest(
                    lastId = lastId,
                    pageSize = 100
                ),
                filter = GetStudentsListFilterParamDto(
                    course = setOf(2),
                    group = null,
                    logType = null,
                    logApprovalStatus = null,
                    positionType = null,
                    positionName = null
                )
            )

            val response = studentsService.getStudentsList(request)
            allStudents.addAll(response.responseCollection)

            lastId = response.lastId

        } while (response.responseCollection.isNotEmpty())

        return allStudents
    }
}