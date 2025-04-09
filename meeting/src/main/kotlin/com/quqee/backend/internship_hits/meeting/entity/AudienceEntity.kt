package com.quqee.backend.internship_hits.meeting.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*


/**
 * Сущность аудитории
 */
@Entity
@Table(name = "audience")
data class AudienceEntity(

    @Id
    @Column(name = "id", nullable = false)
    var id: UUID,

    @Column(name = "building_id", nullable = false)
    var buildingId: UUID,

    @Column(name = "name", nullable = false)
    var name: String,

    ) {
    constructor() : this(
        UUID.randomUUID(),
        UUID.randomUUID(),
        "",
    )
}