package com.quqee.backend.internship_hits.logs.entity

import jakarta.persistence.*
import java.util.*

/**
 * Сущность связи лога и реакции
 */
@Entity
@Table(name = "log_reactions")
data class LogReaction (
    @Id
    val id: UUID,

    @Column(name = "log_id", nullable = false)
    val logId: UUID,

    @Column(name = "reaction_id", nullable = false)
    val reactionId: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID
)