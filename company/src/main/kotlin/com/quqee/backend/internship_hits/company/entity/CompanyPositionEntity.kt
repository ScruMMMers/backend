package com.quqee.backend.internship_hits.company.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*


/**
 * Сущность позиции в компании
 */
@Entity
@Table(name = "company_position")
data class CompanyPositionEntity(

    @Id
    @Column(name = "id", nullable = false)
    val positionId: UUID,

    @Column(name = "company_id", nullable = false)
    val companyId: UUID,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "employed_count", nullable = false)
    val employedCount: Int,

    @Column(name = "interviews_count", nullable = false)
    val interviewsCount: Int

)