package com.quqee.backend.internship_hits.logs.repository

import com.quqee.backend.internship_hits.logs.entity.LogEntity
import com.quqee.backend.internship_hits.logs.entity.TagEntity
import com.quqee.backend.internship_hits.logs.mapper.LogMapper
import com.quqee.backend.internship_hits.logs.repository.jpa.LogsJpaRepository
import com.quqee.backend.internship_hits.public_interface.enums.LogTypeEnum
import com.quqee.backend.internship_hits.public_interface.logs.LogDto
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
    fun getLogsByCurrentUser(lastId: Int?, pageSize: Int): List<LogDto> {
        // TODO: заменить на адекватное получение ID
        val currentUserId = getCurrentUserId()
        return getLogsByUserId(currentUserId, lastId, pageSize)
    }

    /**
     * Получение логов конкретного пользователя
     */
    fun getLogsByUserId(userId: UUID, lastId: Int?, pageSize: Int): List<LogDto> {
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
    fun createLog(message: String, tags: List<TagEntity>, type: LogTypeEnum): LogDto {
        // TODO: заменить на адекватное получение ID
        val currentUserId = getCurrentUserId()
        val now = OffsetDateTime.now()
        
        val logEntity = LogEntity(
            id = UUID.randomUUID(),
            userId = currentUserId,
            message = message,
            tags = tags,
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
    fun updateLog(logId: UUID, message: String, tags: List<TagEntity>, type: LogTypeEnum): LogDto {
        val existingLog = logsJpaRepository.findById(logId)
            .orElseThrow { NoSuchElementException("Лог с ID $logId не найден") }
        
        val updatedLog = existingLog.copy(
            message = message,
            tags = tags,
            type = type,
            editedAt = OffsetDateTime.now()
        )
        
        val savedLog = logsJpaRepository.save(updatedLog)
        return logMapper.toLogView(savedLog)
    }

    /**
     * Получение текущего пользователя (заглушка, заменить на нормальный способ)
     */
    private fun getCurrentUserId(): UUID {
        // TODO: Заменить на реальный способ получения ID текущего пользователя
        return UUID.randomUUID()
    }
} 