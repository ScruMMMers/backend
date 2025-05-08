package com.quqee.backend.internship_hits.tags.repository

import com.quqee.backend.internship_hits.tags.entity.TagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface TagJpaRepository : JpaRepository<TagEntity, UUID> {
    fun findByNameContainingIgnoreCase(name: String): List<TagEntity>
    fun findByCompanyId(companyId: UUID): TagEntity?
}