package com.quqee.backend.internship_hits.position.repository

import com.quqee.backend.internship_hits.position.entity.PositionEntity
import com.quqee.backend.internship_hits.public_interface.common.enums.Position
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PositionJpaRepository : JpaRepository<PositionEntity, Long> {
    @Query("SELECT p FROM PositionEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    fun findByPartName(@Param("name") name: String): List<PositionEntity>

    @Query(
        """
            select count(s.user_id) from students s
            where s.company_id = :companyId
            and s.position_id = :positionId
            and s.is_on_academic_leave = false
        """, nativeQuery = true
    )
    fun getActiveEmployers(
        @Param("companyId") companyId: UUID,
        @Param("positionId") positionId: Long
    ): Int

    @Query(
        """
            select count(s.user_id) from students s
            where s.company_id = :companyId
            and s.is_on_academic_leave = false
        """, nativeQuery = true
    )
    fun getActiveAllEmployers(
        @Param("companyId") companyId: UUID
    ): Int
}