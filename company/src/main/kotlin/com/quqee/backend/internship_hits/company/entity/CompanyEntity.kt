package com.quqee.backend.internship_hits.company.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.net.URI
import java.util.*


/**
 * Сущность компании
 */
@Entity
@Table(name = "company")
data class CompanyEntity(

    @Id
    @Column(name = "id", nullable = false)
    val companyId: UUID,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "agent", nullable = false)
    val agent: UUID,

    @Column(name = "avatar_id", nullable = false)
    val avatarId: UUID,

    @Column(name = "since_year", nullable = false)
    val sinceYear: String,

    @Column(name = "description", nullable = false)
    val description: String,

    @Column(name = "primary_color", nullable = false)
    val primaryColor: String

)