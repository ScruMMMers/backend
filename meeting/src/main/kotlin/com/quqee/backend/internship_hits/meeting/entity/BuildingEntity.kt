package com.quqee.backend.internship_hits.meeting.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*


/**
 * Сущность корпуса
 */
@Entity
@Table(name = "building")
data class BuildingEntity(

    @Id
    @Column(name = "id", nullable = false)
    var id: UUID,

    @Column(name = "name", nullable = false)
    var name: String,

    ) {
    constructor() : this(
        UUID.randomUUID(),
        "",
    )
}