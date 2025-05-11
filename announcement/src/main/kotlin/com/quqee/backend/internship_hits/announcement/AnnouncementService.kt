package com.quqee.backend.internship_hits.announcement

import com.quqee.backend.internship_hits.public_interface.announcement_interface.CreateAnnouncementByFilterDto
import com.quqee.backend.internship_hits.public_interface.announcement_interface.CreateAnnouncementDto
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.public_interface.notification_public.CreateNotificationDto
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import com.quqee.backend.internship_hits.public_interface.students_public.GetStudentsListDto
import com.quqee.backend.internship_hits.students.StudentsService
import org.springframework.stereotype.Service
import java.util.concurrent.Semaphore

interface AnnouncementService {
    fun sendAnnouncementByUserId(dto: CreateAnnouncementDto)
    fun sendAnnouncementByFilter(dto: CreateAnnouncementByFilterDto)
    fun sendSystemAnnouncementByUserId(dto: CreateAnnouncementDto)
}

@Service
class AnnouncementServiceImpl(
    private val announcementBatchDispatcher: AnnouncementBatchDispatcher,
    private val studentsService: StudentsService,
) : AnnouncementService {
    private val maxConcurrentBatches = 5

    override fun sendAnnouncementByUserId(dto: CreateAnnouncementDto) {
        val notificationDtos = dto.userIds.map { userId ->
            CreateNotificationDto(
                title = dto.data.title,
                message = dto.data.text,
                userId = userId,
                type = NotificationType.DEANERY,
                channels = setOf(NotificationChannel.PUSH, NotificationChannel.EMAIL),
                pollId = dto.data.pollId
            )
        }

        announcementBatchDispatcher.sendBatchAsync(notificationDtos)
    }


    override fun sendSystemAnnouncementByUserId(dto: CreateAnnouncementDto) {
        val notificationDtos = dto.userIds.map { userId ->
            CreateNotificationDto(
                title = dto.data.title,
                message = dto.data.text,
                userId = userId,
                type = NotificationType.SYSTEM,
                channels = setOf(NotificationChannel.PUSH, NotificationChannel.EMAIL),
                pollId = dto.data.pollId
            )
        }

        announcementBatchDispatcher.sendBatchAsync(notificationDtos)
    }

    override fun sendAnnouncementByFilter(dto: CreateAnnouncementByFilterDto) {
        val semaphore = Semaphore(maxConcurrentBatches)
        var lastId: UserId? = null
        val pageSize = 20
        val excludedSet = dto.excludeUserIds?.toSet()

        do {
            val studentsPage = studentsService.getStudentsList(
                GetStudentsListDto(
                    pagination = LastIdPaginationRequest(lastId = lastId),
                    filter = dto.filter
                )
            )

            val students = studentsPage.responseCollection
            if (students.isEmpty()) break

            val filteredStudents = if (!excludedSet.isNullOrEmpty()) {
                students.filter { it.id !in excludedSet }
            } else {
                students
            }

            if (filteredStudents.isEmpty()) {
                lastId = studentsPage.lastId
                continue
            }

            val notificationDtos = filteredStudents.map { student ->
                CreateNotificationDto(
                    title = dto.data.title,
                    message = dto.data.text,
                    userId = student.id,
                    type = NotificationType.DEANERY,
                    channels = setOf(NotificationChannel.PUSH, NotificationChannel.EMAIL),
                    pollId = dto.data.pollId
                )
            }

            semaphore.acquire()

            announcementBatchDispatcher.sendBatchAsync(notificationDtos) {
                semaphore.release()
            }

            lastId = studentsPage.lastId
        } while (students.size == pageSize)
    }

}