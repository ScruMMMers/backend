package com.quqee.backend.internship_hits.company.repository

import com.quqee.backend.internship_hits.company.entity.CompanyEntity
import com.quqee.backend.internship_hits.company.mapper.CompanyMapper
import com.quqee.backend.internship_hits.company.repository.jpa.CompanyJpaRepository
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.company.CompanyDto
import com.quqee.backend.internship_hits.public_interface.company.CreateCompanyDto
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.util.*

@Component
class CompanyRepository(
    private val companyJpaRepository: CompanyJpaRepository,
    private val mapper: CompanyMapper
) {

    /**
     * Создание компании
     */
    fun createCompany(createCompanyDto: CreateCompanyDto): CompanyDto {
        val companyEntity = CompanyEntity(
            companyId = UUID.randomUUID(),
            name = createCompanyDto.name,
            agent = createCompanyDto.agentId,
            sinceYear = createCompanyDto.sinceYear,
            description = createCompanyDto.description,
            primaryColor = createCompanyDto.primaryColor.hexColor,
            positions = mutableListOf(),
            createdAt = OffsetDateTime.now()
        )

        return mapper.toCompanyDto(
            companyJpaRepository.save(companyEntity)
        )
    }

    /**
     * Получение компании по идентификатору
     */
    fun getCompany(companyId: UUID): CompanyDto {
        return mapper.toCompanyDto(
            companyJpaRepository.findById(companyId).orElse(null)
        )
    }

    /**
     * Получение списка компаний
     */
    fun getCompaniesList(name: String?, lastId: UUID?, pageSize: Int): List<ShortCompanyDto> {
        var lastCompanyCreatedAt: OffsetDateTime? = null
        if (lastId != null) {
            lastCompanyCreatedAt = companyJpaRepository.findById(lastId)
                .orElseThrow { throw NoSuchElementException("Компания с ID $lastId не найдена") }
                .createdAt
        }

        val companies = companyJpaRepository.findAllByNameLikePage(name, lastCompanyCreatedAt, pageSize)

        return companies.map { mapper.toShortCompanyDto(it) }
    }

}