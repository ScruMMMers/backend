package com.quqee.backend.internship_hits.company.repository.jpa

import com.quqee.backend.internship_hits.company.entity.CompanyEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

@Repository
interface CompanyJpaRepository : JpaRepository<CompanyEntity, UUID> {

    fun findByNameContainingIgnoreCaseOrderByCreatedAtDesc(
        name: String?,
        pageable: Pageable
    ): List<CompanyEntity>

    fun findByNameContainingIgnoreCaseAndCreatedAtLessThanOrderByCreatedAtDesc(
        name: String?,
        createdAt: OffsetDateTime,
        pageable: Pageable
    ): List<CompanyEntity>
}