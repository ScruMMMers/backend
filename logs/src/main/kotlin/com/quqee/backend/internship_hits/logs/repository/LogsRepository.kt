package com.quqee.backend.internship_hits.logs.repository

import com.quqee.backend.internship_hits.logs.entity.LogEntity
import com.quqee.backend.internship_hits.logs.entity.TagEntity
import com.quqee.backend.internship_hits.logs.mapper.LogMapper
import com.quqee.backend.internship_hits.logs.repository.jpa.LogsJpaRepository
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionType
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.logs.LogDto
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.*

@Component
class LogsRepository(
    private val logsJpaRepository: LogsJpaRepository,
    private val logMapper: LogMapper
) {
    /**
     * Получение логов текущего пользователя
     */
    fun getLogsByCurrentUser(lastId: UUID?, pageSize: Int): List<LogDto> {
        val currentUserId = KeycloakUtils.getUserId() ?: throw IllegalArgumentException("userId is null")
        return getLogsByUserId(currentUserId, lastId, pageSize)
    }

    /**
     * Получение логов конкретного пользователя
     */
    fun getLogsByUserId(userId: UUID, lastId: UUID?, pageSize: Int): List<LogDto> {
        val pageable = PageRequest.of(0, pageSize)

        val logs = if (lastId != null) {
            val lastLog = logsJpaRepository.findById(lastId).orElse(null)
                ?: throw NoSuchElementException("Лог с ID $lastId не найден")

            logsJpaRepository.findByUserIdAndCreatedAtLessThanOrderByCreatedAtDescIdDesc(
                userId,
                lastLog.createdAt,
                pageable
            )
        } else {
            logsJpaRepository.findByUserIdOrderByCreatedAtDescIdDesc(userId, pageable)
        }

        return logs.map { logMapper.toLogView(it) }
    }

    /**
     * Создание нового лога
     */
    fun createLog(message: String, tags: List<TagEntity>, type: LogType): LogDto {
        val currentUserId = KeycloakUtils.getUserId() ?: throw IllegalArgumentException("userId is null")
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
    fun updateLog(logId: UUID, message: String, tags: List<TagEntity>, type: LogType): LogDto {
        val currentUserId = KeycloakUtils.getUserId() ?: throw IllegalArgumentException("userId is null")
        val existingLog = logsJpaRepository.findById(logId)
            .orElseThrow { NoSuchElementException("Лог с ID $logId не найден") }

        if (existingLog.userId != currentUserId) {
            throw ExceptionInApplication(ExceptionType.FORBIDDEN)
        }

        val updatedLog = existingLog.copy(
            message = message,
            tags = tags,
            type = type,
            editedAt = OffsetDateTime.now()
        )
        
        val savedLog = logsJpaRepository.save(updatedLog)
        return logMapper.toLogView(savedLog)
    }
} 