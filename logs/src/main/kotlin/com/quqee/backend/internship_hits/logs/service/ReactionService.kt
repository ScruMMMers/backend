package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.logs.entity.LogReaction
import com.quqee.backend.internship_hits.logs.repository.jpa.LogReactionJpaRepository
import com.quqee.backend.internship_hits.logs.repository.jpa.ReactionJpaRepository
import com.quqee.backend.internship_hits.model.rest.ReactionView
import com.quqee.backend.internship_hits.model.rest.RoleEnum
import com.quqee.backend.internship_hits.model.rest.ShortAccountView
import org.springframework.stereotype.Service
import java.util.*

interface ReactionService {
    fun getLogReactions(logId: UUID): List<ReactionView>
    fun addReactionToLog(logId: UUID, reactionId: UUID): ReactionView
    fun removeReactionFromLog(logId: UUID)
}

@Service
class ReactionServiceImpl(
    private val logReactionJpaRepository: LogReactionJpaRepository,
    private val reactionJpaRepository: ReactionJpaRepository
) : ReactionService {

    /**
     * Получение всех реакций лога
     */
    override fun getLogReactions(logId: UUID): List<ReactionView> {
        val logReactions = logReactionJpaRepository.findByLogId(logId)
        return logReactions.map { logReaction ->
            getReactionView(logReaction)
        }
    }
    
    /**
     * Добавление реакции к логу
     */
    override fun addReactionToLog(logId: UUID, reactionId: UUID): ReactionView {
        // TODO: заменить на адекватное получение ID текущего пользователя
        val currentUserId = UUID.randomUUID()

        val reaction = reactionJpaRepository.findById(reactionId)
            .orElseThrow { NoSuchElementException("Реакция с ID $reactionId не найдена") }

        val existingReaction = logReactionJpaRepository.findByLogIdAndUserIdAndReactionId(
            logId = logId,
            userId = currentUserId,
            reactionId = reaction.id
        )
        
        if (existingReaction != null) {
            return getReactionView(existingReaction)
        }

        val logReaction = LogReaction(
            id = UUID.randomUUID(),
            logId = logId,
            reactionId = reaction.id,
            userId = currentUserId
        )
        
        logReactionJpaRepository.save(logReaction)
        return getReactionView(logReaction)
    }
    
    /**
     * Удаление реакции от лога
     */
    override fun removeReactionFromLog(logId: UUID) {
        // TODO: заменить на адекватное получение ID текущего пользователя
        val currentUserId = UUID.randomUUID()
        
        logReactionJpaRepository.deleteByLogIdAndUserId(logId, currentUserId)
    }
    
    /**
     * Преобразование сущности реакции во View
     */
    private fun getReactionView(logReaction: LogReaction): ReactionView {
        // TODO: заменить на получение реальных данных о пользователе

        val shortAccount = ShortAccountView(
            userId = logReaction.userId,
            fullName = "Пользователь ${logReaction.userId}",
            avatarUrl = java.net.URI.create("https://example.com/avatar.png"),
            roles = listOf(RoleEnum.STUDENT_SECOND),
            primaryColor = "#533af9"
        )

        val reaction = reactionJpaRepository.findById(logReaction.reactionId)
            .orElseThrow { NoSuchElementException("Реакция с ID ${logReaction.reactionId} не найдена") }
        
        return ReactionView(
            shortAccount = shortAccount,
            emoji = reaction.emoji
        )
    }
} 