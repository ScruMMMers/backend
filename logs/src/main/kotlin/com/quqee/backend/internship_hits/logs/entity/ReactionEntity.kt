package com.quqee.backend.internship_hits.logs.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "reactions")
data class ReactionEntity(
    @Id
    var id: UUID,

    @Column(name = "emoji", nullable = false)
    var emoji: String
) {
    constructor() : this(UUID.randomUUID(), "")
}
