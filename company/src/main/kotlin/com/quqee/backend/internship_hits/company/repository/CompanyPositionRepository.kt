package com.quqee.backend.internship_hits.company.repository

import com.quqee.backend.internship_hits.company.mapper.CompanyPositionMapper
import com.quqee.backend.internship_hits.company.repository.jpa.CompanyPositionJpaRepository
import org.springframework.stereotype.Repository

@Repository
class CompanyPositionRepository(
    private val companyPositionJpaRepository: CompanyPositionJpaRepository,
    private val mapper: CompanyPositionMapper
) {



}