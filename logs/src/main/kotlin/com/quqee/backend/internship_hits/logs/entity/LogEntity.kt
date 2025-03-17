package com.quqee.backend.internship_hits.logs.entity

import com.quqee.backend.internship_hits.model.rest.LogTypeEnum
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
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(name = "message", nullable = false)
    val message: String,

    @ElementCollection
    @CollectionTable(name = "log_tags", joinColumns = [JoinColumn(name = "log_id")])
    @Column(name = "tag_id")
    val tagIds: List<UUID>,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: LogTypeEnum,

    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime,

    @Column(name = "edited_at", nullable = false)
    val editedAt: OffsetDateTime
) 