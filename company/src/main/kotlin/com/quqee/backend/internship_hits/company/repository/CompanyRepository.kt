package com.quqee.backend.internship_hits.company.repository

import com.quqee.backend.internship_hits.company.entity.CompanyEntity
import com.quqee.backend.internship_hits.company.mapper.CompanyMapper
import com.quqee.backend.internship_hits.company.repository.jpa.CompanyJpaRepository
import com.quqee.backend.internship_hits.model.rest.CompanyView
import com.quqee.backend.internship_hits.model.rest.CreateCompanyView
import com.quqee.backend.internship_hits.model.rest.ShortCompanyView
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CompanyRepository(
    private val companyJpaRepository: CompanyJpaRepository,
    private val mapper: CompanyMapper
) {

    /**
     * Создание компании
     */
    fun createCompany(createCompanyView: CreateCompanyView): CompanyView {
        val companyEntity = CompanyEntity(
            companyId = UUID.randomUUID(),
            name = createCompanyView.name,
            agent = createCompanyView.agentId,
            avatarId = UUID.randomUUID(),
            sinceYear = createCompanyView.sinceYear,
            description = createCompanyView.description,
            primaryColor = createCompanyView.primaryColor
        )

        return mapper.toCompanyView(
            companyJpaRepository.save(companyEntity)
        )
    }

    /**
     * Получение компании по идентификатору
     */
    fun getCompany(companyId: UUID): CompanyView {
        return mapper.toCompanyView(
            companyJpaRepository.findById(companyId).orElse(null)
        )
    }

    /**
     * Получение списка компаний
     */
    fun getCompaniesList(name: String?, lastId: Int?, pageSize: Int): List<ShortCompanyView> {
        val lastUuid = lastId?.let { UUID.fromString(it.toString()) }
        val pageable = PageRequest.of(0, pageSize)

        val companies = companyJpaRepository.findByNameLikeAndCompanyIdLessThan(
            name = name,
            lastId = lastUuid,
            pageable = pageable
        )

        return companies.map { mapper.toShortCompanyView(it) }
    }

}