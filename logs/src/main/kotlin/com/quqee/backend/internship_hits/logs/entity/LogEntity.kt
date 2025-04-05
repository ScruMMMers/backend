package com.quqee.backend.internship_hits.logs.entity

import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.tags.entity.TagEntity
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
    var id: UUID,

    @Column(name = "user_id", nullable = false)
    var userId: UUID,

    @Column(name = "message", nullable = false)
    var message: String,

    @ManyToMany
    @JoinTable(
        name = "log_tags",
        joinColumns = [JoinColumn(name = "log_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: List<TagEntity> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    var type: LogType,

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime,

    @Column(name = "edited_at", nullable = false)
    var editedAt: OffsetDateTime,

    @ElementCollection
    @CollectionTable(name = "log_files", joinColumns = [JoinColumn(name = "log_id")])
    @Column(name = "file_id", nullable = false)
    var fileIds: List<UUID> = mutableListOf(),

    @Column(name = "approval_status", nullable = false)
    @Enumerated(EnumType.STRING)
    var approvalStatus: ApprovalStatus = ApprovalStatus.PENDING,
) {
    constructor() : this(
        UUID.randomUUID(),
        UUID.randomUUID(),
        "",
        mutableListOf(),
        LogType.DEFAULT,
        OffsetDateTime.now(),
        OffsetDateTime.now(),
    )
}