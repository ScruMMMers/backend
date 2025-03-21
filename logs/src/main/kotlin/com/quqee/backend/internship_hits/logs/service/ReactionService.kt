package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.logs.entity.LogReaction
import com.quqee.backend.internship_hits.logs.repository.jpa.LogReactionJpaRepository
import com.quqee.backend.internship_hits.logs.repository.jpa.LogsJpaRepository
import com.quqee.backend.internship_hits.logs.repository.jpa.ReactionJpaRepository
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.common.enums.RoleEnum
import com.quqee.backend.internship_hits.public_interface.reaction.ReactionDto
import org.springframework.stereotype.Service
import java.net.URI
import java.util.*

interface ReactionService {
    fun getLogReactions(logId: UUID): List<ReactionDto>
    fun addReactionToLog(logId: UUID, reactionId: UUID): ReactionDto
    fun removeReactionFromLog(logId: UUID, reactionId: UUID)
}

@Service
class ReactionServiceImpl(
    private val logReactionJpaRepository: LogReactionJpaRepository,
    private val reactionJpaRepository: ReactionJpaRepository,
    private val logJpaRepository: LogsJpaRepository
) : ReactionService {

    /**
     * Получение всех реакций лога
     */
    override fun getLogReactions(logId: UUID): List<ReactionDto> {
        return logReactionJpaRepository.findByLogId(logId)
            .map { getReactionView(it) }
    }

    /**
     * Добавление реакции к логу
     */
    override fun addReactionToLog(logId: UUID, reactionId: UUID): ReactionDto {
        val currentUserId = getCurrentUserId()

        val log = logJpaRepository.findById(logId)
            .orElseThrow { NoSuchElementException("Лог с ID $logId не найден") }

        val reaction = reactionJpaRepository.findById(reactionId)
            .orElseThrow { NoSuchElementException("Реакция с ID $reactionId не найдена") }

        val existingReaction = logReactionJpaRepository.findByLogIdAndUserIdAndReactionId(
            logId, currentUserId, reactionId
        )

        if (existingReaction != null) {
            return getReactionView(existingReaction)
        }

        val logReaction = LogReaction(
            id = UUID.randomUUID(),
            log = log,
            reaction = reaction,
            userId = currentUserId
        )

        logReactionJpaRepository.save(logReaction)
        return getReactionView(logReaction)
    }

    /**
     * Удаление реакции от лога
     */
    override fun removeReactionFromLog(logId: UUID, reactionId: UUID) {
        val currentUserId = getCurrentUserId()

        val logReaction = logReactionJpaRepository.findByLogIdAndUserIdAndReactionId(
            logId, currentUserId, reactionId
        ) ?: throw NoSuchElementException("Реакция не найдена у пользователя")

        logReactionJpaRepository.delete(logReaction)
    }

    /**
     * Преобразование сущности реакции во View
     */
    private fun getReactionView(logReaction: LogReaction): ReactionDto {
        val shortAccount = ShortAccountDto(
            userId = UUID.randomUUID().toString(),
            fullName = "Иван Иванов",
            avatarUrl = URI.create("https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Yandex_icon.svg/2048px-Yandex_icon.svg.png")
                .toString(),
            roles = listOf(RoleEnum.DEANERY),
            primaryColor = "#533af9"
        )
        return ReactionDto(
            shortAccount = shortAccount,
            emoji = logReaction.reaction.emoji
        )
    }

    /**
     * Получение текущего пользователя (заглушка, заменить на нормальный способ)
     */
    private fun getCurrentUserId(): UUID {
        // TODO: Заменить на реальный способ получения ID текущего пользователя
        return UUID.randomUUID()
    }
}