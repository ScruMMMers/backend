package com.quqee.backend.internship_hits.announcement.cron

import com.quqee.backend.internship_hits.announcement.AnnouncementService
import com.quqee.backend.internship_hits.announcement.NotificationTemplates
import com.quqee.backend.internship_hits.public_interface.announcement_interface.AnnouncementDataDto
import com.quqee.backend.internship_hits.public_interface.announcement_interface.CreateAnnouncementDto
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.logs.ShortLogInfo
import com.quqee.backend.internship_hits.public_interface.students_public.GetStudentsListDto
import com.quqee.backend.internship_hits.public_interface.students_public.GetStudentsListFilterParamDto
import com.quqee.backend.internship_hits.public_interface.students_public.StudentDto
import com.quqee.backend.internship_hits.students.StudentsService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.OffsetDateTime

@Service
class AnnouncementCronService(
    private val studentsService: StudentsService,
    private val announcementService: AnnouncementService
) {

    @Scheduled(cron = "0 0 12 * * *")
    fun notifyAllStudents() {
        val students = getStudentList()
        val now = OffsetDateTime.now()

        val noLogs = mutableListOf<UserId>()
        val onlyDefault = mutableListOf<UserId>()
        val afterInterview = mutableListOf<UserId>()
        val afterOffer = mutableListOf<UserId>()

        students.forEach { student ->
            val logs = student.logs
            val logsByType = logs.groupBy { it.type }

            when {
                shouldNotifyNoLogs(logs) -> noLogs += student.id
                shouldNotifyOnlyDefault(logsByType, now) -> onlyDefault += student.id
                shouldNotifyAfterInterview(logsByType, now) -> afterInterview += student.id
                shouldNotifyAfterOffer(logsByType, now) -> afterOffer += student.id
            }
        }

        sendBatchNotification(noLogs, NotificationTemplates.NO_LOGS_TITLE, NotificationTemplates.NO_LOGS_MESSAGE)
        sendBatchNotification(onlyDefault, NotificationTemplates.ONLY_DEFAULT_TITLE, NotificationTemplates.ONLY_DEFAULT_MESSAGE)
        sendBatchNotification(afterInterview, NotificationTemplates.AFTER_INTERVIEW_TITLE, NotificationTemplates.AFTER_INTERVIEW_MESSAGE)
        sendBatchNotification(afterOffer, NotificationTemplates.AFTER_OFFER_TITLE, NotificationTemplates.AFTER_OFFER_MESSAGE)
    }

    private fun getStudentList(): List<StudentDto> {
        val allStudents = mutableListOf<StudentDto>()
        var lastId: UserId? = null

        do {
            val request = GetStudentsListDto(
                pagination = LastIdPaginationRequest(lastId = lastId, pageSize = 100),
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

    private fun shouldNotifyNoLogs(logs: Set<ShortLogInfo>) = logs.isEmpty()

    private fun shouldNotifyOnlyDefault(logsByType: Map<LogType, List<ShortLogInfo>>, now: OffsetDateTime): Boolean {
        if (logsByType.keys == setOf(LogType.DEFAULT)) {
            val lastDefault = logsByType[LogType.DEFAULT]?.maxByOrNull { it.createdAt }
            return lastDefault != null && lastDefault.createdAt.isBefore(now.minusWeeks(1))
        }
        return false
    }

    private fun shouldNotifyAfterInterview(logsByType: Map<LogType, List<ShortLogInfo>>, now: OffsetDateTime): Boolean {
        if (LogType.INTERVIEW in logsByType &&
            LogType.OFFER !in logsByType &&
            LogType.FINAL !in logsByType
        ) {
            val lastInterview = logsByType[LogType.INTERVIEW]?.maxByOrNull { it.createdAt }
            return lastInterview != null && lastInterview.createdAt.isBefore(now.minusWeeks(2))
        }
        return false
    }

    private fun shouldNotifyAfterOffer(logsByType: Map<LogType, List<ShortLogInfo>>, now: OffsetDateTime): Boolean {
        if (LogType.OFFER in logsByType && LogType.FINAL !in logsByType) {
            val lastOffer = logsByType[LogType.OFFER]?.maxByOrNull { it.createdAt }
            return lastOffer != null && lastOffer.createdAt.isBefore(now.minusWeeks(2))
        }
        return false
    }

    private fun sendBatchNotification(userIds: List<UserId>, title: String, message: String) {
        if (userIds.isEmpty()) return

        val dto = CreateAnnouncementDto(
            data = AnnouncementDataDto(
                title = title,
                text = message
            ),
            userIds = userIds
        )

        announcementService.sendSystemAnnouncementByUserId(dto)
    }
}
