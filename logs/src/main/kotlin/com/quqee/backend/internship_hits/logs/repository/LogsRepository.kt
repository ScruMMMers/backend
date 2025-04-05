package com.quqee.backend.internship_hits.logs.repository

import com.quqee.backend.internship_hits.logs.entity.LogEntity
import com.quqee.backend.internship_hits.logs.mapper.LogMapper
import com.quqee.backend.internship_hits.logs.repository.jpa.LogsJpaRepository
import com.quqee.backend.internship_hits.logs.specification.LogSpecification
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.logs.LogDto
import com.quqee.backend.internship_hits.tags.entity.TagEntity
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.*

@Component
class LogsRepository(
    private val logsJpaRepository: LogsJpaRepository,
    private val logMapper: LogMapper
) {
    private val needApprove: MutableList<LogType> = mutableListOf(LogType.FINAL, LogType.PRACTICE_DIARY)

    fun getLogsByCurrentUser(
        lastId: UUID?,
        pageSize: Int,
        logTypes: List<LogType>? = null,
        approvalStatuses: List<ApprovalStatus>? = null
    ): List<LogDto> {
        val currentUserId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "User is null")
        return getLogsByUserId(currentUserId, lastId, pageSize, logTypes, approvalStatuses)
    }

    fun getLogsByUserId(
        userId: UUID,
        lastId: UUID?,
        pageSize: Int,
        logTypes: List<LogType>? = null,
        approvalStatuses: List<ApprovalStatus>? = null
    ): List<LogDto> {
        return getLogsWithSpecs(
            userId = userId,
            lastId = lastId,
            pageSize = pageSize,
            logTypes = logTypes,
            approvalStatuses = approvalStatuses
        )
    }

    fun getAllLogs(
        lastId: UUID?,
        pageSize: Int,
        logTypes: List<LogType>? = null,
        approvalStatuses: List<ApprovalStatus>? = null
    ): List<LogDto> {
        return getLogsWithSpecs(
            userId = null,
            lastId = lastId,
            pageSize = pageSize,
            logTypes = logTypes,
            approvalStatuses = approvalStatuses
        )
    }

    private fun getLogsWithSpecs(
        userId: UUID?,
        lastId: UUID?,
        pageSize: Int,
        logTypes: List<LogType>?,
        approvalStatuses: List<ApprovalStatus>?
    ): List<LogDto> {
        val pageable = PageRequest.of(0, pageSize)

        val createdBefore = lastId?.let {
            logsJpaRepository.findById(it)
                .orElseThrow { ExceptionInApplication(ExceptionType.NOT_FOUND, "Лог с ID $it не найден") }
                .createdAt
        }

        val spec = Specification
            .where(LogSpecification.byUserId(userId))
            .and(LogSpecification.byCreatedAtBefore(createdBefore))
            .and(LogSpecification.byLogTypes(logTypes))
            .and(LogSpecification.byApprovalStatuses(approvalStatuses))

        val logs = logsJpaRepository.findAll(spec, pageable)
        return logs.content.map { logMapper.toLogDto(it) }
    }

    fun createLog(message: String, tags: List<TagEntity>, type: LogType, files: List<UUID>): LogDto {
        val currentUserId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "User is null")
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
            approvalStatus = getApprovalStatus(type)
        )

        val savedLog = logsJpaRepository.save(logEntity)
        return logMapper.toLogDto(savedLog)
    }

    fun updateLog(logId: UUID, message: String, tags: List<TagEntity>, type: LogType, files: List<UUID>): LogDto {
        val currentUserId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "User is null")

        val existingLog = logsJpaRepository.findById(logId)
            .orElseThrow { ExceptionInApplication(ExceptionType.NOT_FOUND, "Лог с ID $logId не найден") }

        if (existingLog.userId != currentUserId) {
            throw ExceptionInApplication(ExceptionType.FORBIDDEN)
        }

        val updatedLog = existingLog.copy(
            message = message,
            tags = tags,
            type = type,
            editedAt = OffsetDateTime.now(),
            fileIds = files,
            approvalStatus = getApprovalStatus(type)
        )

        val savedLog = logsJpaRepository.save(updatedLog)
        return logMapper.toLogDto(savedLog)
    }

    fun updateApprovalStatus(logId: UUID, isApprove: Boolean): LogDto {
        val log = logsJpaRepository.findById(logId)
            .orElseThrow { ExceptionInApplication(ExceptionType.NOT_FOUND, "Лог с ID $logId не найден") }

        val updatedLog = log.copy(
            approvalStatus = if (isApprove) ApprovalStatus.APPROVED else ApprovalStatus.REJECTED
        )
        val savedLog = logsJpaRepository.save(updatedLog)
        return logMapper.toLogDto(savedLog)
    }

    fun getLogById(logId: UUID): LogDto? {
        val log = logsJpaRepository.findById(logId)
        return log.map { logMapper.toLogDto(it) }.orElse(null)
    }

    private fun getApprovalStatus(logType: LogType): ApprovalStatus {
        return if (needApprove.contains(logType)) {
            ApprovalStatus.PENDING
        } else {
            ApprovalStatus.APPROVED
        }
    }
}
