package com.quqee.backend.internship_hits.company.repository.jpa

import com.quqee.backend.internship_hits.company.entity.CompanyEntity
import com.quqee.backend.internship_hits.company.entity.CompanyPositionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CompanyPositionJpaRepository : JpaRepository<CompanyPositionEntity, UUID> {

    fun findFirstByCompanyAndPositionId(company: CompanyEntity, positionId: Long): Optional<CompanyPositionEntity>

    @Query(
        "SELECT c FROM CompanyPositionEntity c WHERE c.company.companyId = :companyId AND c.positionId = :positionId"
    )
    fun findByCompanyAndPositionId(companyId: UUID, positionId: Long): Optional<CompanyPositionEntity>

}