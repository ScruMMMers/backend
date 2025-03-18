package com.quqee.backend.internship_hits.logs.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "tags")
data class TagEntity (
    @Id
    val id: UUID,

    @Column(name = "companyId", nullable = false)
    val companyId: UUID,

    @Column(name = "name", nullable = false)
    val name: String
)