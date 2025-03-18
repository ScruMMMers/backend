package com.quqee.backend.internship_hits.logs.mapper

import com.quqee.backend.internship_hits.logs.entity.LogEntity
import com.quqee.backend.internship_hits.logs.service.ReactionService
import com.quqee.backend.internship_hits.model.rest.*
import org.springframework.stereotype.Component
import java.net.URI
import java.util.*

@Component
class LogMapper(
    private val reactionService: ReactionService
) {
    /**
     * Преобразование сущности лога в DTO представление
     */
    fun toLogView(entity: LogEntity): LogView {
        return LogView(
            id = entity.id,
            message = entity.message,
            tags = getTagsForLog(entity.tagIds),
            type = entity.type,
            createdAt = entity.createdAt,
            editedAt = entity.editedAt,
            reactions = reactionService.getLogReactions(entity.id),
            comments = getCommentsForLog(entity.id)
        )
    }
    
    /**
     * Получение тегов для лога
     * ПОКА ЗАГЛУШКА
     */
    private fun getTagsForLog(tagIds: List<UUID>): List<TagView> {
        return tagIds.map { tagId ->
            TagView(
                id = tagId,
                name = "Тег $tagId",
                shortCompany = ShortCompanyView(
                    companyId = UUID.randomUUID(),
                    name = "Компания",
                    avatarUrl = URI.create("https://example.com/avatar.png"),
                    primaryColor = "#ef1818"
                )
            )
        }
    }
    
    /**
     * Получение комментариев для лога
     * ПОКА ЗАГЛУШКА
     */
    private fun getCommentsForLog(logId: UUID): List<CommentView> {
        // Для примера возвращаем два комментария
        val commentId1 = UUID.randomUUID()
        return listOf(
            CommentView(
                id = commentId1,
                message = "Отличный лог!",
                createdAt = java.time.OffsetDateTime.now().minusHours(1),
                updatedAt = java.time.OffsetDateTime.now().minusHours(1),
                shortAccount = ShortAccountView(
                    userId = UUID.randomUUID(),
                    fullName = "Петр Петров",
                    avatarUrl = URI.create("https://example.com/avatar2.png"),
                    roles = listOf(RoleEnum.STUDENT_SECOND),
                    primaryColor = "#ff8fe9"
                ),
                replyTo = null
            ),
            CommentView(
                id = UUID.randomUUID(),
                message = "Согласен!",
                createdAt = java.time.OffsetDateTime.now().minusMinutes(30),
                updatedAt = java.time.OffsetDateTime.now().minusMinutes(30),
                shortAccount = ShortAccountView(
                    userId = UUID.randomUUID(),
                    fullName = "Сергей Сергеев",
                    avatarUrl = URI.create("https://example.com/avatar3.png"),
                    roles = listOf(RoleEnum.DEANERY),
                    primaryColor = "#533af9"
                ),
                replyTo = commentId1
            )
        )
    }
} 