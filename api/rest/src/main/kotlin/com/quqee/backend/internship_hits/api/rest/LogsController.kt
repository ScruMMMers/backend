package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.logs.service.LogsService
import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.public_interface.logs.CreateLogRequestDto
import com.quqee.backend.internship_hits.public_interface.logs.CreatedLogDto
import com.quqee.backend.internship_hits.public_interface.logs.LogListDto
import com.quqee.backend.internship_hits.public_interface.logs.UpdateLogRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class LogsController(
    private val logsService: LogsService,
    private val mapCreateLogRequest: FromApiToInternalMapper<CreateLogRequestView, CreateLogRequestDto>,
    private val mapUpdateLogRequest: FromApiToInternalMapper<UpdateLogRequestView, UpdateLogRequestDto>,
    private val mapCreatedLog: FromInternalToApiMapper<CreatedLogView, CreatedLogDto>,
    private val mapLogsList: FromInternalToApiMapper<LogsListView, LogListDto>
) : LogsApiDelegate {

    override fun logsLogIdPost(
        logId: UUID,
        updateLogRequestView: UpdateLogRequestView
    ): ResponseEntity<CreatedLogView> {
        val dto = mapUpdateLogRequest.fromApi(updateLogRequestView)
        val updatedLog = logsService.updateLog(logId, dto)
        return ResponseEntity.ok(mapCreatedLog.fromInternal(updatedLog))
    }

    override fun logsMeGet(lastId: UUID?, size: Int?): ResponseEntity<LogsListView> {
        val logsListDto = logsService.getMyLogs(lastId, size)
        return ResponseEntity.ok(mapLogsList.fromInternal(logsListDto))
    }

    override fun logsPost(createLogRequestView: CreateLogRequestView): ResponseEntity<CreatedLogView> {
        val dto = mapCreateLogRequest.fromApi(createLogRequestView)
        val createdLog = logsService.createLog(dto)
        return ResponseEntity.ok(mapCreatedLog.fromInternal(createdLog))
    }

    override fun logsUserIdGet(userId: UUID, lastId: UUID?, size: Int?): ResponseEntity<LogsListView> {
        val logsListDto = logsService.getUserLogs(userId, lastId, size)
        return ResponseEntity.ok(mapLogsList.fromInternal(logsListDto))
    }
}
