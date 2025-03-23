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
    var id: UUID,

    @ManyToOne
    @JoinColumn(name = "log_id", nullable = false)
    var log: LogEntity,

    @ManyToOne
    @JoinColumn(name = "reaction_id", nullable = false)
    var reaction: ReactionEntity,

    @Column(name = "user_id", nullable = false)
    var userId: UUID
) {
    constructor() : this(UUID.randomUUID(), LogEntity(), ReactionEntity(), UUID.randomUUID())
}