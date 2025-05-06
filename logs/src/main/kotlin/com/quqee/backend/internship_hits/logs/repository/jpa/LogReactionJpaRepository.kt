package com.quqee.backend.internship_hits.logs.repository.jpa

import com.quqee.backend.internship_hits.logs.entity.LogReaction
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LogReactionJpaRepository : JpaRepository<LogReaction, UUID> {
    fun findByLogId(logId: UUID): List<LogReaction>
    fun findByLogIdAndUserIdAndReactionId(
        logId: UUID,
        userId: UUID,
        reactionId: UUID
    ): LogReaction?
    fun deleteByLogIdAndUserIdAndId(logId: UUID, userId: UUID, id: UUID)
} 