package com.quqee.backend.internship_hits.logs.repository.jpa

import com.quqee.backend.internship_hits.logs.entity.LogEntity
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

@Repository
interface LogsJpaRepository : JpaRepository<LogEntity, UUID> {
    @Query("""
        SELECT l FROM LogEntity l 
        WHERE l.userId = :userId 
          AND (:logTypes IS NULL OR l.type IN :logTypes)
          AND (:approvalStatuses IS NULL OR l.approvalStatus IN :approvalStatuses)
        ORDER BY l.createdAt DESC, l.id DESC
    """)
    fun findByUserIdAndFilters(
        @Param("userId") userId: UUID,
        @Param("logTypes") logTypes: List<LogType>?,
        @Param("approvalStatuses") approvalStatuses: List<ApprovalStatus>?,
        pageable: Pageable
    ): List<LogEntity>

    @Query("""
        SELECT l FROM LogEntity l 
        WHERE l.userId = :userId 
          AND l.createdAt < :createdAt
          AND (:logTypes IS NULL OR l.type IN :logTypes)
          AND (:approvalStatuses IS NULL OR l.approvalStatus IN :approvalStatuses)
        ORDER BY l.createdAt DESC, l.id DESC
    """)
    fun findByUserIdAndCreatedAtLessThanAndFilters(
        @Param("userId") userId: UUID,
        @Param("createdAt") createdAt: OffsetDateTime,
        @Param("logTypes") logTypes: List<LogType>?,
        @Param("approvalStatuses") approvalStatuses: List<ApprovalStatus>?,
        pageable: Pageable
    ): List<LogEntity>
}