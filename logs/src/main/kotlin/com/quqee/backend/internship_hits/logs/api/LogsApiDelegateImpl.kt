package com.quqee.backend.internship_hits.logs.api

import com.quqee.backend.internship_hits.api.rest.LogsApiDelegate
import com.quqee.backend.internship_hits.logs.service.LogsService
import com.quqee.backend.internship_hits.model.rest.*
import org.springframework.context.annotation.Primary
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Primary
@Component
class LogsApiDelegateImpl(
    private val logsService: LogsService
) : LogsApiDelegate {
    
    override fun logsMeGet(lastId: Int?, size: Int?): ResponseEntity<LogsListView> {
        val logs = logsService.getMyLogs(lastId, size)
        return ResponseEntity.ok(logs)
    }
    
    override fun logsUserIdGet(userId: UUID, lastId: Int?, size: Int?): ResponseEntity<LogsListView> {
        val logs = logsService.getUserLogs(userId, lastId, size)
        return ResponseEntity.ok(logs)
    }
    
    override fun logsPost(createLogRequestView: CreateLogRequestView): ResponseEntity<CreatedLogView> {
        val createdLog = logsService.createLog(createLogRequestView)
        return ResponseEntity.ok(createdLog)
    }
    
    override fun logsLogIdPost(logId: UUID, updateLogRequestView: UpdateLogRequestView): ResponseEntity<CreatedLogView> {
        val updatedLog = logsService.updateLog(logId, updateLogRequestView)
        return ResponseEntity.ok(updatedLog)
    }
} 