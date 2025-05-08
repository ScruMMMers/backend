package com.quqee.backend.internship_hits.marks.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "mark")
data class MarkEntity(

    @Id
    @Column(name = "id", nullable = false)
    var id: UUID,

    @Column(name = "user_id", nullable = false)
    var userId: UUID,

    @Column(name = "mark", nullable = false)
    var mark: Int,

    @Column(name = "date", nullable = false)
    var date: OffsetDateTime,

    @Column(name = "semester", nullable = false)
    var semester: Int,

    ) {
    constructor() : this(
        UUID.randomUUID(),
        UUID.randomUUID(),
        2,
        OffsetDateTime.now(),
        2
    )
}
