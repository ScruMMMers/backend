package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.logs.repository.LogsRepository
import com.quqee.backend.internship_hits.logs.repository.jpa.TagJpaRepository
import com.quqee.backend.internship_hits.model.rest.*
import org.springframework.stereotype.Service
import java.util.*

interface LogsService {
    fun getMyLogs(lastId: Int?, size: Int?): LogsListView
    fun getUserLogs(userId: UUID, lastId: Int?, size: Int?): LogsListView
    fun createLog(createLogRequest: CreateLogRequestView): CreatedLogView
    fun updateLog(logId: UUID, updateLogRequest: UpdateLogRequestView): CreatedLogView
}

@Service
class LogsServiceImpl (
    private val logsRepository: LogsRepository,
    private val tagJpaRepository: TagJpaRepository
) : LogsService {

    /**
     * Получение логов текущего пользователя
     */
    override fun getMyLogs(lastId: Int?, size: Int?): LogsListView {
        val pageSize = size ?: 20
        val logs = logsRepository.getLogsByCurrentUser(lastId, pageSize)
        val hasNext = logs.size >= pageSize
        
        return LogsListView(
            logs = logs,
            page = LastIdPaginationView(
                lastId = if (logs.isNotEmpty()) logs.last().id.toString() else null,
                pageSize = pageSize,
                hasNext = hasNext
            )
        )
    }

    /**
     * Получение логов конкретного пользователя
     */
    override fun getUserLogs(userId: UUID, lastId: Int?, size: Int?): LogsListView {
        val pageSize = size ?: 20
        val logs = logsRepository.getLogsByUserId(userId, lastId, pageSize)
        val hasNext = logs.size >= pageSize
        
        return LogsListView(
            logs = logs,
            page = LastIdPaginationView(
                lastId = if (logs.isNotEmpty()) logs.last().id.toString() else null,
                pageSize = pageSize,
                hasNext = hasNext
            )
        )
    }

    /**
     * Создание нового лога
     */
    override fun createLog(createLogRequest: CreateLogRequestView): CreatedLogView {
        val newLog = logsRepository.createLog(
            message = createLogRequest.message,
            tagIds = emptyList(),
            type = createLogRequest.type
        )
        
        return CreatedLogView(log = newLog)
    }

    /**
     * Обновление существующего лога
     */
    override fun updateLog(logId: UUID, updateLogRequest: UpdateLogRequestView): CreatedLogView {
        val updatedLog = logsRepository.updateLog(
            logId = logId,
            message = updateLogRequest.message,
            tagIds = findTagIdsByNames(extractTagsFromMessage(updateLogRequest.message)),
            type = updateLogRequest.type
        )
        
        return CreatedLogView(log = updatedLog)
    }

    /**
     * Извлечение тегов из сообщения
     */
    private fun extractTagsFromMessage(message: String): List<String> {
        return message.split(" ")
            .filter { it.startsWith("@") }
    }

    /**
     * Поиск тегов по имени и возврат их UUID
     */
    private fun findTagIdsByNames(tagNames: List<String>): List<UUID> {
        val tagIds = mutableListOf<UUID>()
        for (name in tagNames) {
            val tags = tagJpaRepository.findByName(name)
            tagIds.addAll(tags.map { it.id })
        }
        return tagIds
    }
} 