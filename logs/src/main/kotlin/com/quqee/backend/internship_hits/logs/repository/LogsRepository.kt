package com.quqee.backend.internship_hits.logs.repository

import com.quqee.backend.internship_hits.logs.entity.LogEntity
import com.quqee.backend.internship_hits.logs.mapper.LogMapper
import com.quqee.backend.internship_hits.logs.repository.jpa.LogsJpaRepository
import com.quqee.backend.internship_hits.model.rest.LogTypeEnum
import com.quqee.backend.internship_hits.model.rest.LogView
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

@Repository
class LogsRepository(
    private val logsJpaRepository: LogsJpaRepository,
    private val logMapper: LogMapper
) {
    /**
     * Получение логов текущего пользователя
     */
    fun getLogsByCurrentUser(lastId: Int?, pageSize: Int): List<LogView> {
        // TODO: заменить на адекватное получение ID
        val currentUserId = UUID.randomUUID()
        return getLogsByUserId(currentUserId, lastId, pageSize)
    }

    /**
     * Получение логов конкретного пользователя
     */
    fun getLogsByUserId(userId: UUID, lastId: Int?, pageSize: Int): List<LogView> {
        val lastIdUuid = lastId?.let { UUID.fromString(it.toString()) }
        val pageable = PageRequest.of(0, pageSize)
        
        val logs = logsJpaRepository.findByUserIdAndIdLessThanOrderByCreatedAtDesc(
            userId = userId,
            lastId = lastIdUuid,
            pageable = pageable
        )
        
        return logs.map { logMapper.toLogView(it) }
    }

    /**
     * Создание нового лога
     */
    fun createLog(message: String, tagIds: List<UUID>, type: LogTypeEnum): LogView {
        // TODO: заменить на адекватное получение ID
        val currentUserId = UUID.randomUUID()
        val now = OffsetDateTime.now()
        
        val logEntity = LogEntity(
            id = UUID.randomUUID(),
            userId = currentUserId,
            message = message,
            tagIds = tagIds,
            type = type,
            createdAt = now,
            editedAt = now
        )
        
        val savedLog = logsJpaRepository.save(logEntity)
        return logMapper.toLogView(savedLog)
    }

    /**
     * Обновление существующего лога
     */
    fun updateLog(logId: UUID, message: String, tagIds: List<UUID>, type: LogTypeEnum): LogView {
        val existingLog = logsJpaRepository.findById(logId)
            .orElseThrow { NoSuchElementException("Лог с ID $logId не найден") }
        
        val updatedLog = existingLog.copy(
            message = message,
            tagIds = tagIds,
            type = type,
            editedAt = OffsetDateTime.now()
        )
        
        val savedLog = logsJpaRepository.save(updatedLog)
        return logMapper.toLogView(savedLog)
    }
} 