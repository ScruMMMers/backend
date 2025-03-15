package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.model.rest.CreateLogRequestView
import com.quqee.backend.internship_hits.model.rest.CreatedLogView
import com.quqee.backend.internship_hits.model.rest.LogsListView
import com.quqee.backend.internship_hits.model.rest.UpdateLogRequestView
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class LogsController : LogsApiDelegate {
    override fun logsLogIdPost(
        logId: UUID,
        updateLogRequestView: UpdateLogRequestView
    ): ResponseEntity<CreatedLogView> {
        return super.logsLogIdPost(logId, updateLogRequestView)
    }

    override fun logsMeGet(lastId: Int?, size: Int?): ResponseEntity<LogsListView> {
        return super.logsMeGet(lastId, size)
    }

    override fun logsPost(createLogRequestView: CreateLogRequestView): ResponseEntity<CreatedLogView> {
        return super.logsPost(createLogRequestView)
    }

    override fun logsUserIdGet(userId: UUID, lastId: Int?, size: Int?): ResponseEntity<LogsListView> {
        return super.logsUserIdGet(userId, lastId, size)
    }
}