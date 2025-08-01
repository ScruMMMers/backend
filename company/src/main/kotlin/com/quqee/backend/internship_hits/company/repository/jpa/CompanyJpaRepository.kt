package com.quqee.backend.internship_hits.company.repository.jpa

import com.quqee.backend.internship_hits.company.entity.CompanyEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CompanyJpaRepository : JpaRepository<CompanyEntity, UUID>, JpaSpecificationExecutor<CompanyEntity> {

    @Query("SELECT c FROM CompanyEntity c WHERE c.name = :name AND c.isDeleted = false")
    fun findByName(@Param("name") name: String): Optional<CompanyEntity>

    @Query("SELECT c FROM CompanyEntity c WHERE c.companyId IN :ids AND c.isDeleted = false")
    fun findByIds(@Param("ids") ids: List<UUID>): List<CompanyEntity>
}