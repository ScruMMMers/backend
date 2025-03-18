package com.quqee.backend.internship_hits.logs.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "reactions")
data class ReactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID,

    @Column(name = "emoji", nullable = false)
    val emoji: String
)
