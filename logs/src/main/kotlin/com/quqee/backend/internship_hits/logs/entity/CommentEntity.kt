package com.quqee.backend.internship_hits.logs.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "comment")
data class CommentEntity(

    @Id
    val id: UUID,

    @Column(name = "author_id", nullable = false)
    val author: UUID,

    @Column(name = "message", nullable = false)
    var message: String,

    @Column(name = "reply_to", nullable = false)
    val replyTo: UUID,

    @Column(name = "created_at", nullable = false)
    val createdAt: OffsetDateTime,

    @Column(name = "updated_at", nullable = true)
    var updatedAt: OffsetDateTime?,

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean

) {
    constructor() : this(
        UUID.randomUUID(),
        UUID.randomUUID(),
        "",
        UUID.randomUUID(),
        OffsetDateTime.now(),
        OffsetDateTime.now(),
        false
    )
}
