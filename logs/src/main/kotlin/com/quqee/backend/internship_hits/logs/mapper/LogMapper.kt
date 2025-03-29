package com.quqee.backend.internship_hits.logs.mapper

import com.quqee.backend.internship_hits.file.service.FileService
import com.quqee.backend.internship_hits.logs.entity.LogEntity
import com.quqee.backend.internship_hits.logs.service.CommentService
import com.quqee.backend.internship_hits.logs.service.ReactionService
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
import com.quqee.backend.internship_hits.public_interface.logs.LogDto
import com.quqee.backend.internship_hits.public_interface.profile_public.GetProfileDto
import com.quqee.backend.internship_hits.public_interface.common.TagDto
import com.quqee.backend.internship_hits.tags.entity.TagEntity
import org.springframework.stereotype.Component
import java.net.URI
import java.time.OffsetDateTime
import java.util.*

@Component
class LogMapper(
    private val reactionService: ReactionService,
    private val profileService: ProfileService,
    private val commentService: CommentService,
    private val fileService: FileService
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
            files = entity.fileIds.map { fileService.getFileById(it) }
        )
    }
    
    /**
     * Получение тегов для лога
     * ПОКА ЗАГЛУШКА
     */
    private fun getTagsForLog(tags: List<TagEntity>): List<TagDto> {
        return tags.map { tag ->
            TagDto(
                id = tag.id,
                name = tag.name,
                shortCompany = ShortCompanyDto(
                    companyId = UUID.randomUUID(),
                    name = "Яндекс",
                    avatarUrl = URI.create("https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Yandex_icon.svg/2048px-Yandex_icon.svg.png"),
                    primaryColor = ColorEnum.NAVY,
                    createdAt = OffsetDateTime.now()
                )
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