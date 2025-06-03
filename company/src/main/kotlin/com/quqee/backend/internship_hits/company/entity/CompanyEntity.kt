package com.quqee.backend.internship_hits.company.entity

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*


/**
 * Сущность компании
 */
@Entity
@Table(name = "company")
data class CompanyEntity(

    @Id
    @Column(name = "id", nullable = false)
    var companyId: UUID,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "avatar_id", nullable = false)
    var avatarId: UUID,

    @ElementCollection
    @CollectionTable(name = "employees_companies", joinColumns = [JoinColumn(name = "company_id")])
    @Column(name = "user_id", nullable = true)
    var employees: List<UUID> = mutableListOf(),

    @Column(name = "since_year", nullable = false)
    var sinceYear: String,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "primary_color", nullable = false)
    var primaryColor: String,

    @OneToMany(mappedBy = "company", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var positions: List<CompanyPositionEntity> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime,

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean

) {
    constructor() : this(
        UUID.randomUUID(),
        "",
        UUID.randomUUID(),
        mutableListOf(),
        "",
        "",
        "",
        mutableListOf(),
        OffsetDateTime.now(),
        false
    )
}