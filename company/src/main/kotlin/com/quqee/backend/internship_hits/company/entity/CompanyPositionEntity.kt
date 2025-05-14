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
    var id: UUID,

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    var company: CompanyEntity,

    @Column(name = "position_id", nullable = false)
    var positionId: Long,

    @Column(name = "interviews_count", nullable = false)
    var interviewsCount: Int,

    @Column(name = "employed_count", nullable = false)
    var employedCount: Int

    ) {
    constructor() : this(
        UUID.randomUUID(),
        CompanyEntity(),
        0L,
        0,
        0
    )
}