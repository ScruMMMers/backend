package com.quqee.backend.internship_hits.company.entity

import jakarta.persistence.*
import java.util.*


/**
 * Сущность позиции в компании
 */
@Entity
@Table(name = "company_position")
data class CompanyPositionEntity(

    @Id
    @Column(name = "id", nullable = false)
    var positionId: UUID,

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    var company: CompanyEntity,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "employed_count", nullable = false)
    var employedCount: Int,

    @Column(name = "interviews_count", nullable = false)
    var interviewsCount: Int

) {
    constructor() : this(
        UUID.randomUUID(),
        CompanyEntity(),
        "",
        0,
        0
    )
}