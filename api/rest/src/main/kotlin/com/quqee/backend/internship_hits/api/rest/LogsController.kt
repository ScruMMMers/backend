package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.logs.service.CommentService
import com.quqee.backend.internship_hits.logs.service.LogsService
import com.quqee.backend.internship_hits.mapper.EnumerationMapper
import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.public_interface.comment.CommentsListDto
import com.quqee.backend.internship_hits.public_interface.comment.CreateCommentDto
import com.quqee.backend.internship_hits.public_interface.comment.UpdateCommentDto
import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
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
    private val commentService: CommentService,
    private val mapCreateLogRequest: FromApiToInternalMapper<CreateLogRequestView, CreateLogRequestDto>,
    private val mapUpdateLogRequest: FromApiToInternalMapper<UpdateLogRequestView, UpdateLogRequestDto>,
    private val mapCreatedLog: FromInternalToApiMapper<CreatedLogView, CreatedLogDto>,
    private val mapLogsList: FromInternalToApiMapper<LogsListView, LogListDto>,
    private val mapCommentList: FromInternalToApiMapper<CommentsListView, CommentsListDto>,
    private val mapComment: FromInternalToApiMapper<CommentView, CommentDto>,
    private val mapCreateComment: FromApiToInternalMapper<CreateCommentView, CreateCommentDto>,
    private val mapUpdateComment: FromApiToInternalMapper<UpdateCommentView, UpdateCommentDto>,
    private val mapLogType: EnumerationMapper<LogTypeEnum, LogType>,
    private val mapApprovalStatus: EnumerationMapper<ApprovalStatusEnum, ApprovalStatus>
) : LogsApiDelegate {

    override fun logsLogIdPost(
        logId: UUID,
        updateLogRequestView: UpdateLogRequestView
    ): ResponseEntity<CreatedLogView> {
        val dto = mapUpdateLogRequest.fromApi(updateLogRequestView)
        val updatedLog = logsService.updateLog(logId, dto)
        return ResponseEntity.ok(mapCreatedLog.fromInternal(updatedLog))
    }

    override fun logsMeGet(
        lastId: UUID?,
        size: Int?,
        logTypes: List<LogTypeEnum>?,
        approvalStatuses: List<ApprovalStatusEnum>?
    ): ResponseEntity<LogsListView> {
        val logTypesInternal = logTypes?.map { logType -> mapLogType.mapToInternal(logType) }
        val approvalStatusesInternal = approvalStatuses?.map { approvalStatus -> mapApprovalStatus.mapToInternal(approvalStatus) }

        val logsListDto = logsService.getMyLogs(lastId, size, logTypesInternal, approvalStatusesInternal)
        return ResponseEntity.ok(mapLogsList.fromInternal(logsListDto))
    }

    override fun logsPost(createLogRequestView: CreateLogRequestView): ResponseEntity<CreatedLogView> {
        val dto = mapCreateLogRequest.fromApi(createLogRequestView)
        val createdLog = logsService.createLog(dto)
        return ResponseEntity.ok(mapCreatedLog.fromInternal(createdLog))
    }

    override fun logsUserIdGet(
        userId: UUID,
        lastId: UUID?,
        size: Int?,
        logTypes: List<LogTypeEnum>?,
        approvalStatuses: List<ApprovalStatusEnum>?
    ): ResponseEntity<LogsListView> {
        val logTypesInternal = logTypes?.map { logType -> mapLogType.mapToInternal(logType) }
        val approvalStatusesInternal = approvalStatuses?.map { approvalStatus -> mapApprovalStatus.mapToInternal(approvalStatus) }

        val logsListDto = logsService.getUserLogs(userId, lastId, size, logTypesInternal, approvalStatusesInternal)
        return ResponseEntity.ok(mapLogsList.fromInternal(logsListDto))
    }

    override fun logsLogIdApprovePost(
        logId: UUID,
        approveLogRequestView: ApproveLogRequestView
    ): ResponseEntity<CreatedLogView> {
        val dto = logsService.updateApprovalStatus(logId, approveLogRequestView.isApproved)
        return ResponseEntity.ok(mapCreatedLog.fromInternal(dto))
    }

    override fun logsLogIdCommentsGet(
        logId: UUID,
        isDeleted: Boolean?,
        lastId: UUID?,
        size: Int?
    ): ResponseEntity<CommentsListView> {
        return ResponseEntity.ok(
            mapCommentList.fromInternal(
                commentService.getCommentsList(
                    logId,
                    isDeleted,
                    lastId,
                    size
                )
            )
        )
    }

    override fun logsLogIdCommentPost(logId: UUID, createCommentView: CreateCommentView): ResponseEntity<CommentView> {
        val createCommentDto = mapCreateComment.fromApi(createCommentView)
        return ResponseEntity.ok(mapComment.fromInternal(commentService.createComment(logId, createCommentDto)))
    }

    override fun logsLogIdCommentCommentIdPut(
        logId: UUID,
        commentId: UUID,
        updateCommentView: UpdateCommentView
    ): ResponseEntity<CommentView> {
        val updateCommentDto = mapUpdateComment.fromApi(updateCommentView)
        return ResponseEntity.ok(
            mapComment.fromInternal(
                commentService.updateComment(
                    logId,
                    commentId,
                    updateCommentDto
                )
            )
        )
    }

    override fun logsLogIdCommentCommentIdDeleteDelete(
        logId: UUID,
        commentId: UUID
    ): ResponseEntity<CommentView> {
        return ResponseEntity.ok(mapComment.fromInternal(commentService.deleteComment(logId, commentId)))
    }
}