package com.quqee.backend.internship_hits.position.repository

import com.quqee.backend.internship_hits.position.entity.PositionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PositionJpaRepository : JpaRepository<PositionEntity, Long> {
    @Query("SELECT p FROM PositionEntity p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    fun findByPartName(@Param("name") name: String): List<PositionEntity>
}