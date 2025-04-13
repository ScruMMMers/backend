package com.quqee.backend.internship_hits.internship.repository

import com.quqee.backend.internship_hits.internship.entity.InternshipEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface InternshipJpaRepository : JpaRepository<InternshipEntity, UUID> {

    fun findByUserId(userId: UUID): InternshipEntity?

    fun findAllByCompanyId(companyId: UUID): List<InternshipEntity>
}
