package com.quqee.backend.internship_hits.logs.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "tags")
data class TagEntity (
    @Id
    var id: UUID,

    @Column(name = "companyId", nullable = false)
    var companyId: UUID,

    @Column(name = "name", nullable = false)
    var name: String
) {
    constructor() : this(UUID.randomUUID(), UUID.randomUUID(), "")
}