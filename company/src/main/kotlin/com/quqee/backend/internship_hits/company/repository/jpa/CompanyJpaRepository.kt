package com.quqee.backend.internship_hits.company.repository.jpa

import com.quqee.backend.internship_hits.company.entity.CompanyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

@Repository
interface CompanyJpaRepository : JpaRepository<CompanyEntity, UUID> {

    @Query(
        value = """
        SELECT * FROM company c
        WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:createdAt IS NULL OR c.created_at < :createdAt)
        ORDER BY c.created_at DESC
        LIMIT :limit
    """,
        nativeQuery = true
    )
    fun findAllByNameLikePage(
        @Param("name") name: String?,
        @Param("createdAt") createdAt: OffsetDateTime?,
        @Param("limit") limit: Int
    ): List<CompanyEntity>
}