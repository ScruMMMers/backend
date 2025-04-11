package com.quqee.backend.internship_hits.logs.mapper

import com.quqee.backend.internship_hits.file.service.FileService
import com.quqee.backend.internship_hits.logs.entity.LogEntity
import com.quqee.backend.internship_hits.logs.service.CommentService
import com.quqee.backend.internship_hits.logs.service.ReactionService
import com.quqee.backend.internship_hits.position.mapper.PositionMapper
import com.quqee.backend.internship_hits.position.service.PositionService
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.logs.LogDto
import com.quqee.backend.internship_hits.public_interface.profile_public.GetProfileDto
import com.quqee.backend.internship_hits.tags_query.service.TagQueryService
import org.springframework.stereotype.Component
import java.util.*

@Component
class LogMapper(
    private val reactionService: ReactionService,
    private val profileService: ProfileService,
    private val commentService: CommentService,
    private val fileService: FileService,
    private val tagQueryService: TagQueryService,
    private val positionMapper: PositionMapper
) {
    /**
     * Преобразование сущности лога в DTO представление
     */
    fun toLogDto(entity: LogEntity): LogDto {
        return LogDto(
            id = entity.id,
            message = entity.message,
            tags = tagQueryService.mapTagEntityToDto(entity.tags).tags,
            hashtags = entity.hashtags.map { positionMapper.toDto(it) },
            type = entity.type,
            createdAt = entity.createdAt,
            editedAt = entity.editedAt,
            reactions = reactionService.getLogReactions(entity.id),
            comments = getCommentsForLog(entity.id),
            author = profileService.getShortAccount(GetProfileDto(userId = entity.userId)),
            files = entity.fileIds.map { fileService.getFileById(it) },
            approvalStatus = entity.approvalStatus,
        )
    }

    /**
     * Получение комментариев для лога
     */
    private fun getCommentsForLog(logId: UUID): List<CommentDto> {
        return commentService.getCommentsList(
            logId = logId,
            isDeleted = null,
            lastId = null,
            size = null
        ).comments
    }
} 