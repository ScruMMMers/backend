package com.quqee.backend.internship_hits.internship.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "internships")
data class InternshipEntity (
    @Id
    var id: UUID,

    @Column(name = "user_id", nullable = false)
    var userId: UUID,

    @Column(name = "company_id", nullable = false)
    var companyId: UUID,

    @Column(name = "position_id", nullable = true)
    var positionId: Long,

    @Column(name = "started_at", nullable = false)
    var startedAt: OffsetDateTime,
): Serializable {
    constructor() : this(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 0, OffsetDateTime.now())
}