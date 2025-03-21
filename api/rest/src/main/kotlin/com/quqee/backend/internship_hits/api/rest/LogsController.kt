package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.logs.service.LogsService
import com.quqee.backend.internship_hits.model.rest.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class LogsController(
    private val logsService: LogsService
) : LogsApiDelegate {
    override fun logsLogIdPost(
        logId: UUID,
        updateLogRequestView: UpdateLogRequestView
    ): ResponseEntity<CreatedLogView> {
        return ResponseEntity.ok(
            logsService.updateLog(logId, updateLogRequestView)
        )
    }

    override fun logsMeGet(lastId: Int?, size: Int?): ResponseEntity<LogsListView> {
        return ResponseEntity.ok(
            logsService.getMyLogs(lastId, size)
        )
    }

    override fun logsPost(createLogRequestView: CreateLogRequestView): ResponseEntity<CreatedLogView> {
        return ResponseEntity.ok(
            logsService.createLog(createLogRequestView)
        )
    }

    override fun logsUserIdGet(userId: UUID, lastId: Int?, size: Int?): ResponseEntity<LogsListView> {
        return ResponseEntity.ok(
            logsService.getUserLogs(userId, lastId, size)
        )
    }
}