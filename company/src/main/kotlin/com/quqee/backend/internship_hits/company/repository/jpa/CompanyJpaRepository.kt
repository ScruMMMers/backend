package com.quqee.backend.internship_hits.company.repository.jpa

import com.quqee.backend.internship_hits.company.entity.CompanyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CompanyJpaRepository : JpaRepository<CompanyEntity, UUID>, JpaSpecificationExecutor<CompanyEntity> {
    fun findByName(name: String): Optional<CompanyEntity>
}