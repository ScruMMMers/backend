package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.logs.entity.LogReaction
import com.quqee.backend.internship_hits.logs.repository.jpa.LogReactionJpaRepository
import com.quqee.backend.internship_hits.logs.repository.jpa.LogsJpaRepository
import com.quqee.backend.internship_hits.logs.repository.jpa.ReactionJpaRepository
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.profile_public.GetProfileDto
import com.quqee.backend.internship_hits.public_interface.reaction.PossibleReactionDto
import com.quqee.backend.internship_hits.public_interface.reaction.ReactionDto
import org.springframework.stereotype.Service
import java.util.*

interface ReactionService {
    fun getLogReactions(logId: UUID): List<ReactionDto>
    fun addReactionToLog(logId: UUID, reactionId: UUID): ReactionDto
    fun removeReactionFromLog(logId: UUID, reactionId: UUID)
    fun getAllPossibleReactions(): List<PossibleReactionDto>
}

@Service
class ReactionServiceImpl(
    private val logReactionJpaRepository: LogReactionJpaRepository,
    private val reactionJpaRepository: ReactionJpaRepository,
    private val logJpaRepository: LogsJpaRepository,
    private val profileService: ProfileService,
) : ReactionService {

    /**
     * Получение всех возможных реакций
     */
    override fun getAllPossibleReactions(): List<PossibleReactionDto> {
        return reactionJpaRepository.findAll().map { reaction ->
            PossibleReactionDto(
                id = reaction.id,
                emoji = reaction.emoji
            )
        }
    }

    /**
     * Получение всех реакций лога
     */
    override fun getLogReactions(logId: UUID): List<ReactionDto> {
        return logReactionJpaRepository.findByLogId(logId)
            .map { getReactionDto(it) }
    }

    /**
     * Добавление реакции к логу
     */
    override fun addReactionToLog(logId: UUID, reactionId: UUID): ReactionDto {
        val currentUserId = KeycloakUtils.getUserId() ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "User is null")

        val log = logJpaRepository.findById(logId)
            .orElseThrow { throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Лог с ID $logId не найден") }

        val reaction = reactionJpaRepository.findById(reactionId)
            .orElseThrow { throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Реакция с ID $reactionId не найден") }

        val existingReaction = logReactionJpaRepository.findByLogIdAndUserIdAndReactionId(
            logId, currentUserId, reactionId
        )

        if (existingReaction != null) {
            return getReactionDto(existingReaction)
        }

        val logReaction = LogReaction(
            id = UUID.randomUUID(),
            log = log,
            reaction = reaction,
            userId = currentUserId
        )

        logReactionJpaRepository.save(logReaction)
        return getReactionDto(logReaction)
    }

    /**
     * Удаление реакции от лога
     */
    override fun removeReactionFromLog(logId: UUID, reactionId: UUID) {
        val currentUserId = KeycloakUtils.getUserId() ?:
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "UserId is null")

        val logReaction = logReactionJpaRepository.findByLogIdAndUserIdAndReactionId(
            logId, currentUserId, reactionId
        ) ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Reaction with id $reactionId not found")

        logReactionJpaRepository.delete(logReaction)
    }

    /**
     * Преобразование сущности реакции во View
     */
    private fun getReactionDto(logReaction: LogReaction): ReactionDto {
        val shortAccount = profileService.getShortAccount(GetProfileDto(userId = logReaction.userId))
        return ReactionDto(
            id = logReaction.id,
            shortAccount = shortAccount,
            emoji = logReaction.reaction.emoji
        )
    }
}