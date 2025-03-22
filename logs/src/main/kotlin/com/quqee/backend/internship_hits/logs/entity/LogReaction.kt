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

    @ManyToOne
    @JoinColumn(name = "log_id", nullable = false)
    val log: LogEntity,

    @ManyToOne
    @JoinColumn(name = "reaction_id", nullable = false)
    val reaction: ReactionEntity,

    @Column(name = "user_id", nullable = false)
    val userId: UUID
)