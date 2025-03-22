package com.quqee.backend.internship_hits.logs.repository.jpa

import com.quqee.backend.internship_hits.logs.entity.TagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TagJpaRepository : JpaRepository<TagEntity, UUID> {
    fun findByName(name: String): List<TagEntity>
    fun findByNameContainingIgnoreCase(name: String): List<TagEntity>
}