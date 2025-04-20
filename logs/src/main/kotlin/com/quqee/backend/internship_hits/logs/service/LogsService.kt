package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.logs.message.KafkaSender
import com.quqee.backend.internship_hits.logs.repository.LogsRepository
import com.quqee.backend.internship_hits.position.entity.PositionEntity
import com.quqee.backend.internship_hits.position.service.PositionService
import com.quqee.backend.internship_hits.public_interface.common.CompanyStatisticsProjection
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.logs.*
import com.quqee.backend.internship_hits.public_interface.message.logs.NewInternshipDto
import com.quqee.backend.internship_hits.public_interface.message.logs.NewLogDto
import com.quqee.backend.internship_hits.tags.entity.TagEntity
import com.quqee.backend.internship_hits.tags_query.service.TagQueryService
import org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE
import org.springframework.stereotype.Service
import java.util.*

interface LogsService {
    fun getMyLogs(
        lastId: UUID?, 
        size: Int?, 
        logTypes: List<LogType>? = null, 
        approvalStatuses: List<ApprovalStatus>? = null
    ): LogListDto
    
    fun getUserLogs(
        userId: UUID, 
        lastId: UUID?, 
        size: Int?, 
        logTypes: List<LogType>? = null, 
        approvalStatuses: List<ApprovalStatus>? = null
    ): LogListDto

    fun getAllLogs(
        lastId: UUID?,
        size: Int?,
        logTypes: List<LogType>? = null,
        approvalStatuses: List<ApprovalStatus>? = null
    ): LogListDto
    
    fun createLog(createLogRequest: CreateLogRequestDto): CreatedLogDto
    fun updateLog(logId: UUID, updateLogRequest: UpdateLogRequestDto): CreatedLogDto
    fun getLogById(logId: UUID): LogDto
    fun updateApprovalStatus(logId: UUID, isApprove: Boolean): CreatedLogDto
    fun changeCompanyLog(changeCompanyRequest: ChangeCompanyDto): CreatedLogDto
    fun getLogsForStatistic(companyId: UUID): List<CompanyStatisticsProjection>
}

@Service
class LogsServiceImpl (
    private val logsRepository: LogsRepository,
    private val tagQueryService: TagQueryService,
    private val positionService: PositionService,
    private val newInternshipSender: KafkaSender<NewInternshipDto>,
    private val newLogSender: KafkaSender<NewLogDto>,
) : LogsService {

    /**
     * Получение логов текущего пользователя с фильтрацией по типам и статусам одобрения
     */
    override fun getMyLogs(
        lastId: UUID?, 
        size: Int?, 
        logTypes: List<LogType>?, 
        approvalStatuses: List<ApprovalStatus>?
    ): LogListDto {
        val pageSize = size ?: DEFAULT_PAGE_SIZE
        val logs = logsRepository.getLogsByCurrentUser(lastId, pageSize, logTypes, approvalStatuses)
        val hasNext = logs.size >= pageSize
        
        return LogListDto(
            logs = logs,
            page = LastIdPagination(
                lastId = if (logs.isNotEmpty()) logs.last().id else null,
                pageSize = pageSize,
                hasNext = hasNext
            )
        )
    }

    /**
     * Получение логов конкретного пользователя с фильтрацией по типам и статусам одобрения
     */
    override fun getUserLogs(
        userId: UUID, 
        lastId: UUID?, 
        size: Int?, 
        logTypes: List<LogType>?, 
        approvalStatuses: List<ApprovalStatus>?
    ): LogListDto {
        val pageSize = size ?: DEFAULT_PAGE_SIZE
        val logs = logsRepository.getLogsByUserId(userId, lastId, pageSize, logTypes, approvalStatuses)
        val hasNext = logs.size >= pageSize
        
        return LogListDto(
            logs = logs,
            page = LastIdPagination(
                lastId = if (logs.isNotEmpty()) logs.last().id else null,
                pageSize = pageSize,
                hasNext = hasNext
            )
        )
    }

    override fun getAllLogs(
        lastId: UUID?,
        size: Int?,
        logTypes: List<LogType>?,
        approvalStatuses: List<ApprovalStatus>?
    ): LogListDto {
        val pageSize = size ?: DEFAULT_PAGE_SIZE
        val logs = logsRepository.getAllLogs(lastId, pageSize, logTypes, approvalStatuses)
        val hasNext = logs.size >= pageSize

        return LogListDto(
            logs = logs,
            page = LastIdPagination(
                lastId = if (logs.isNotEmpty()) logs.last().id else null,
                pageSize = pageSize,
                hasNext = hasNext
            )
        )
    }

    /**
     * Создание нового лога
     */
    override fun createLog(createLogRequest: CreateLogRequestDto): CreatedLogDto {
        val newLog = logsRepository.createLog(
            message = createLogRequest.message,
            tags = findTagsByNames(extractTagsFromMessage(createLogRequest.message)),
            hashtags = findPositionsByNames(extractHashtagsFromMessage(createLogRequest.message)),
            type = createLogRequest.type,
            files = createLogRequest.files ?: emptyList()
        )

        newLogSender.send(
            NewLogDto(
                userId = newLog.author.userId,
                logType = newLog.type
            )
        )

        return CreatedLogDto(log = newLog)
    }

    /**
     * Обновление существующего лога
     */
    override fun updateLog(logId: UUID, updateLogRequest: UpdateLogRequestDto): CreatedLogDto {
        val updatedLog = logsRepository.updateLog(
            logId = logId,
            message = updateLogRequest.message,
            tags = findTagsByNames(extractTagsFromMessage(updateLogRequest.message)),
            hashtags = findPositionsByNames(extractHashtagsFromMessage(updateLogRequest.message)),
            type = updateLogRequest.type,
            files = updateLogRequest.files ?: emptyList()
        )
        
        return CreatedLogDto(log = updatedLog)
    }

    /**
     * Получаем лог по id
     */
    override fun getLogById(logId: UUID): LogDto {
        return logsRepository.getLogById(logId) ?:
            throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Log with id $logId not found")
    }

    /**
     * Аппрувнуть лог
     */
    override fun updateApprovalStatus(logId: UUID, isApprove: Boolean): CreatedLogDto {
        val log = logsRepository.updateApprovalStatus(logId, isApprove)
        return CreatedLogDto(log = log)
    }

    /**
     * Получить статистику по логам
     */
    override fun changeCompanyLog(changeCompanyRequest: ChangeCompanyDto): CreatedLogDto {
        val tag = tagQueryService.getTagByCompanyId(changeCompanyRequest.companyId)
        val hashtag = positionService.getPositionEntityById(changeCompanyRequest.positionId)
        val companyChangeLog = logsRepository.createLog(
            message = "Сменил(а) компанию на @${tag.name} на позицию #${hashtag.name}",
            tags = listOf(tag),
            hashtags = listOf(hashtag),
            type = LogType.COMPANY_CHANGE,
            files = emptyList()
        )

        newInternshipSender.send(
            NewInternshipDto(
                userId = companyChangeLog.author.userId,
                companyId = tag.companyId,
                positionId = hashtag.id
            )
        )

        return CreatedLogDto(log = companyChangeLog)
    }

    /**
     * Получить статистику по логам
     */
    override fun getLogsForStatistic(companyId: UUID): List<CompanyStatisticsProjection> {
        return logsRepository.getLogsForStatistic(companyId)
    }

    /**
     * Извлечение тегов из сообщения
     */
    private fun extractTagsFromMessage(message: String): List<String> {
        return message.split(" ")
            .filter { it.startsWith("@") }
            .map { it.removePrefix("@") }
    }

    /**
     * Поиск тегов по имени и возврат их сущностей
     */
    private fun findTagsByNames(tagNames: List<String>): List<TagEntity> {
        return tagNames
            .flatMap { name -> tagQueryService.getTagsEntityByNamePart(name) }
            .distinct()
    }

    /**
     * Извлечение хэштегов из сообщения
     */
    private fun extractHashtagsFromMessage(message: String): List<String> {
        return message.split(" ")
            .filter { it.startsWith("#") }
            .map { it.removePrefix("#") }
    }

    /**
     * Поиск хэштегов по имени и возврат их сущностей
     */
    private fun findPositionsByNames(names: List<String>): List<PositionEntity> {
        return names
            .flatMap { name -> positionService.getPositionEntityByPartName(name) }
            .distinct()
    }
} 