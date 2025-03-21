package com.quqee.backend.internship_hits.logs.entity

import com.quqee.backend.internship_hits.public_interface.enums.LogTypeEnum
import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

/**
 * Сущность лога
 */
@Entity
@Table(name = "logs")
data class LogEntity(
    @Id
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "message", nullable = false)
    val message: String,

    @ManyToMany
    @JoinTable(
        name = "log_tags",
        joinColumns = [JoinColumn(name = "log_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    val tags: List<TagEntity> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: LogTypeEnum,

    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime,

    @Column(name = "edited_at", nullable = false)
    val editedAt: OffsetDateTime
) 