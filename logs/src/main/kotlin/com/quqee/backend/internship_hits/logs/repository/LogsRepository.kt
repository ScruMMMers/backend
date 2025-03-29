package com.quqee.backend.internship_hits.logs.repository

import com.quqee.backend.internship_hits.logs.entity.LogEntity
import com.quqee.backend.internship_hits.logs.mapper.LogMapper
import com.quqee.backend.internship_hits.logs.repository.jpa.LogsJpaRepository
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.logs.LogDto
import com.quqee.backend.internship_hits.tags.entity.TagEntity
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
        val currentUserId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "User is null")
        return getLogsByUserId(currentUserId, lastId, pageSize)
    }

    /**
     * Получение логов конкретного пользователя
     */
    fun getLogsByUserId(userId: UUID, lastId: UUID?, pageSize: Int): List<LogDto> {
        val pageable = PageRequest.of(0, pageSize)

        val logs = if (lastId != null) {
            val lastLog = logsJpaRepository.findById(lastId).orElse(null)
                ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Лог с ID $lastId не найден")

            logsJpaRepository.findByUserIdAndCreatedAtLessThanOrderByCreatedAtDescIdDesc(
                userId,
                lastLog.createdAt,
                pageable
            )
        } else {
            logsJpaRepository.findByUserIdOrderByCreatedAtDescIdDesc(userId, pageable)
        }

        return logs.map { logMapper.toLogDto(it) }
    }

    /**
     * Создание нового лога
     */
    fun createLog(message: String, tags: List<TagEntity>, type: LogType, files: List<UUID>): LogDto {
        val currentUserId = KeycloakUtils.getUserId() ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "User is null")
        val now = OffsetDateTime.now()
        
        val logEntity = LogEntity(
            id = UUID.randomUUID(),
            userId = currentUserId,
            message = message,
            tags = tags,
            type = type,
            createdAt = now,
            editedAt = now,
            fileIds = files,
        )
        
        val savedLog = logsJpaRepository.save(logEntity)
        return logMapper.toLogDto(savedLog)
    }

    /**
     * Обновление существующего лога
     */
    fun updateLog(logId: UUID, message: String, tags: List<TagEntity>, type: LogType, files: List<UUID>): LogDto {
        val currentUserId = KeycloakUtils.getUserId() ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "User is null")
        val existingLog = logsJpaRepository.findById(logId)
            .orElseThrow { NoSuchElementException("Лог с ID $logId не найден") }

        if (existingLog.userId != currentUserId) {
            throw ExceptionInApplication(ExceptionType.FORBIDDEN)
        }

        val updatedLog = existingLog.copy(
            message = message,
            tags = tags,
            type = type,
            editedAt = OffsetDateTime.now(),
            fileIds = files
        )
        
        val savedLog = logsJpaRepository.save(updatedLog)
        return logMapper.toLogDto(savedLog)
    }

    /**
     * Получение лога по ID
     */
    fun getLogById(logId: UUID): LogDto? {
        val log = logsJpaRepository.findById(logId)
        return log.map { logMapper.toLogDto(it) }.orElse(null)
    }
} 