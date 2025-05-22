package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.company.service.CompanyPositionService
import com.quqee.backend.internship_hits.logs.message.KafkaSender
import com.quqee.backend.internship_hits.logs.repository.LogsRepository
import com.quqee.backend.internship_hits.position.entity.PositionEntity
import com.quqee.backend.internship_hits.position.service.PositionService
import com.quqee.backend.internship_hits.public_interface.common.CompanyStatisticsProjection
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.company_position.CreateCompanyPositionDto
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.logs.*
import com.quqee.backend.internship_hits.public_interface.students_public.CreateCompanyToStudentDto
import com.quqee.backend.internship_hits.students.StudentsService
import com.quqee.backend.internship_hits.tags.entity.TagEntity
import com.quqee.backend.internship_hits.tags_query.service.TagQueryService
import org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
open class LogsServiceImpl (
    private val logsRepository: LogsRepository,
    private val tagQueryService: TagQueryService,
    private val positionService: PositionService,
    private val studentsService: StudentsService,
    private val companyPositionService: CompanyPositionService,
    private val kafkaSender: KafkaSender<Any>,
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
        val logs = logsRepository.getAllLogs(lastId, pageSize + 1, logTypes, approvalStatuses)
        val hasNext = logs.size > pageSize

        return LogListDto(
            logs = logs.take(pageSize),
            page = LastIdPagination(
                lastId = if (logs.isNotEmpty()) logs.last().id else null,
                pageSize = pageSize,
                hasNext = hasNext
            )
        )
    }

    @Transactional
    override fun createLog(createLogRequest: CreateLogRequestDto): CreatedLogDto {
        val tags = extractTags(createLogRequest.message)
        val hashtags = extractHashtags(createLogRequest.message)

        // Если тип лога не MESSAGE и не DEFAULT - создаём CompanyPosition
        if (!listOf(LogType.MESSAGE, LogType.DEFAULT).contains(createLogRequest.type)) {
            createCompanyPositions(tags, hashtags)
        }

        val newLog = logsRepository.createLog(
            message = createLogRequest.message,
            tags = tags,
            hashtags = hashtags,
            type = createLogRequest.type,
            files = createLogRequest.files ?: emptyList()
        )

        // Если лог типа INTERVIEW, увеличиваем счетчик интервью по каждой комбинации
        if (createLogRequest.type == LogType.INTERVIEW) {
            incrementInterviewCounters(tags, hashtags)
        }

        return CreatedLogDto(log = newLog)
    }


    override fun updateLog(logId: UUID, updateLogRequest: UpdateLogRequestDto): CreatedLogDto {
        val tags = extractTags(updateLogRequest.message)
        val hashtags = extractHashtags(updateLogRequest.message)

        if (!listOf(LogType.MESSAGE, LogType.DEFAULT).contains(updateLogRequest.type)) {
            createCompanyPositions(tags, hashtags)
        }

        val updatedLog = logsRepository.updateLog(
            logId = logId,
            message = updateLogRequest.message,
            tags = tags,
            hashtags = hashtags,
            type = updateLogRequest.type,
            files = updateLogRequest.files ?: emptyList()
        )

        return CreatedLogDto(log = updatedLog)
    }


    /**
     * Извлечение тегов из сообщения
     */
    private fun extractTags(message: String): List<TagEntity> {
        return findTagsByNames(extractTagsFromMessage(message))
    }

    /**
     * Извлечение хэштегов из сообщения
     */
    private fun extractHashtags(message: String): List<PositionEntity> {
        return findPositionsByNames(extractHashtagsFromMessage(message))
    }


    /**
     * Создание CompanyPosition для каждой комбинации тега и хэштега.
     */
    private fun createCompanyPositions(tags: List<TagEntity>, hashtags: List<PositionEntity>) {
        for (tag in tags) {
            for (hashtag in hashtags) {
                try {
                    companyPositionService.createCompanyPosition(tag.companyId, CreateCompanyPositionDto(hashtag.id))
                } catch (e: Exception) {
                    continue
                }
            }
        }
    }

    /**
     * Увеличение счетчиков интервью для каждой комбинации тега и хэштега.
     */
    private fun incrementInterviewCounters(tags: List<TagEntity>, hashtags: List<PositionEntity>) {
        for (tag in tags) {
            for (hashtag in hashtags) {
                try {
                    companyPositionService.incrementInterviewsCount(tag.companyId, hashtag.id)
                } catch (e: Exception) {
                    continue
                }
            }
        }
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
    @Transactional
    override fun updateApprovalStatus(logId: UUID, isApprove: Boolean): CreatedLogDto {
        val log = logsRepository.updateApprovalStatus(logId, isApprove)

        if (log.type == LogType.FINAL || log.type == LogType.COMPANY_CHANGE){
            if (log.tags.isNotEmpty()){
                studentsService.setCompanyToStudent(
                    CreateCompanyToStudentDto(log.tags[0].shortCompany.companyId, null, log.author.userId)
                )
                if (log.hashtags.isNotEmpty()){
                    try {
                        companyPositionService.incrementEmployedCount(log.tags[0].shortCompany.companyId, log.hashtags[0].id)
                    } catch (e: Exception) {
                        //нужна обработка
                    }
                }
            }
        }
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

//        kafkaSender.send(
//            NewInternshipDto(
//                userId = companyChangeLog.author.userId,
//                companyId = tag.companyId,
//                positionId = hashtag.id
//            )
//        )

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
        val regex = Regex("@([a-zA-Zа-яА-Я0-9_.-]+)")
        return regex.findAll(message).map { it.groupValues[1] }.toList()
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
        val regex = Regex("#([a-zA-Zа-яА-Я0-9_.-]+)")
        return regex.findAll(message).map { it.groupValues[1] }.toList()
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