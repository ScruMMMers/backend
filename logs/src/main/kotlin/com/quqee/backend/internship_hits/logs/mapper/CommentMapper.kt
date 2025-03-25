package com.quqee.backend.internship_hits.logs.mapper

import com.quqee.backend.internship_hits.logs.entity.CommentEntity
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.comment.CreateCommentDto
import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.profile_public.GetProfileDto
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.*

@Component
class CommentMapper(
    private val profileService: ProfileService
) {

    /**
     * Преобразование сущности комментария в DTO представление
     */
    fun toCommentDto(entity: CommentEntity): CommentDto {
        return CommentDto(
            id = entity.id,
            author = profileService.getShortAccount(GetProfileDto(userId = entity.author)),
            message = entity.message,
            replyTo = entity.replyTo,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            isDeleted = entity.isDeleted
        )
    }

    /**
     * Преобразование CreateCommentDto в новую сущность
     */
    fun toCommentEntity(dto: CreateCommentDto, userId: UUID, logId: UUID): CommentEntity {
        return CommentEntity(
            id = UUID.randomUUID(),
            author = userId,
            message = dto.message,
            replyTo = logId,
            createdAt = OffsetDateTime.now(),
            updatedAt = null,
            isDeleted = false
        )
    }

}