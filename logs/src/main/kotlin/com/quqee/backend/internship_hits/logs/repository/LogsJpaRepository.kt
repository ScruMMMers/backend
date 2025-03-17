package com.quqee.backend.internship_hits.logs.repository

import com.quqee.backend.internship_hits.logs.entity.LogEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface LogsJpaRepository : JpaRepository<LogEntity, UUID> {
    
    @Query("""
        SELECT l FROM LogEntity l 
        WHERE l.userId = :userId 
        AND (:lastId IS NULL OR l.id < :lastId)
        ORDER BY l.createdAt DESC
    """)
    fun findByUserIdAndIdLessThanOrderByCreatedAtDesc(
        @Param("userId") userId: UUID,
        @Param("lastId") lastId: UUID?,
        pageable: Pageable
    ): List<LogEntity>
} 