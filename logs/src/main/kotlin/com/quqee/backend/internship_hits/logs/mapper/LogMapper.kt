package com.quqee.backend.internship_hits.logs.mapper

import com.quqee.backend.internship_hits.company.service.CompanyService
import com.quqee.backend.internship_hits.file.service.FileService
import com.quqee.backend.internship_hits.logs.entity.LogEntity
import com.quqee.backend.internship_hits.logs.service.CommentService
import com.quqee.backend.internship_hits.logs.service.ReactionService
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.logs.LogDto
import com.quqee.backend.internship_hits.public_interface.profile_public.GetProfileDto
import com.quqee.backend.internship_hits.public_interface.common.TagDto
import com.quqee.backend.internship_hits.tags.entity.TagEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class LogMapper(
    private val reactionService: ReactionService,
    private val profileService: ProfileService,
    private val commentService: CommentService,
    private val fileService: FileService,
    private val companyService: CompanyService
) {
    /**
     * Преобразование сущности лога в DTO представление
     */
    fun toLogDto(entity: LogEntity): LogDto {
        return LogDto(
            id = entity.id,
            message = entity.message,
            tags = getTagsForLog(entity.tags),
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
     * Получение тегов для лога
     */
    private fun getTagsForLog(tags: List<TagEntity>): List<TagDto> {
        return tags.map { tag ->
            TagDto(
                id = tag.id,
                name = tag.name,
                shortCompany = companyService.getShortCompany(tag.companyId) ?: ShortCompanyDto()
            )
        }
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