package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.logs.repository.LogsRepository
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.logs.*
import com.quqee.backend.internship_hits.tags.entity.TagEntity
import com.quqee.backend.internship_hits.tags.service.TagService
import org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE
import org.springframework.stereotype.Service
import java.util.*

interface LogsService {
    fun getMyLogs(lastId: UUID?, size: Int?): LogListDto
    fun getUserLogs(userId: UUID, lastId: UUID?, size: Int?): LogListDto
    fun createLog(createLogRequest: CreateLogRequestDto): CreatedLogDto
    fun updateLog(logId: UUID, updateLogRequest: UpdateLogRequestDto): CreatedLogDto
    fun getLogById(logId: UUID): LogDto
}

@Service
class LogsServiceImpl (
    private val logsRepository: LogsRepository,
    private val tagService: TagService
) : LogsService {

    /**
     * Получение логов текущего пользователя
     */
    override fun getMyLogs(lastId: UUID?, size: Int?): LogListDto {
        val pageSize = size ?: DEFAULT_PAGE_SIZE
        val logs = logsRepository.getLogsByCurrentUser(lastId, pageSize)
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
     * Получение логов конкретного пользователя
     */
    override fun getUserLogs(userId: UUID, lastId: UUID?, size: Int?): LogListDto {
        val pageSize = size ?: DEFAULT_PAGE_SIZE
        val logs = logsRepository.getLogsByUserId(userId, lastId, pageSize)
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
            type = createLogRequest.type
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
            type = updateLogRequest.type
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
     * Извлечение тегов из сообщения
     */
    private fun extractTagsFromMessage(message: String): List<String> {
        return message.split(" ")
            .filter { it.startsWith("@") }
    }

    /**
     * Поиск тегов по имени и возврат их сущностей
     */
    private fun findTagsByNames(tagNames: List<String>): List<TagEntity> {
        return tagNames
            .flatMap { name -> tagService.getTagsEntityByNamePart(name) }
            .distinct()
    }
} 