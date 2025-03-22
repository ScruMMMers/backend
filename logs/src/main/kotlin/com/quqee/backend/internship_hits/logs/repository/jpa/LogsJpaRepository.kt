package com.quqee.backend.internship_hits.logs.repository.jpa

import com.quqee.backend.internship_hits.logs.entity.LogEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LogsJpaRepository : JpaRepository<LogEntity, UUID> {
    fun findByUserIdAndIdLessThanOrderByCreatedAtDesc(
        userId: UUID,
        lastId: UUID?,
        pageable: Pageable
    ): List<LogEntity>
} 