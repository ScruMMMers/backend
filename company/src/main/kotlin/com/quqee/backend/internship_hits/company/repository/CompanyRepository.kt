package com.quqee.backend.internship_hits.company.repository

import com.quqee.backend.internship_hits.company.entity.CompanyEntity
import com.quqee.backend.internship_hits.company.mapper.CompanyMapper
import com.quqee.backend.internship_hits.company.repository.jpa.CompanyJpaRepository
import com.quqee.backend.internship_hits.company.specification.CompanySpecification
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.company.CompanyDto
import com.quqee.backend.internship_hits.public_interface.company.CreateCompanyDto
import com.quqee.backend.internship_hits.public_interface.company.ShortCompanyWithEmployersDto
import com.quqee.backend.internship_hits.public_interface.company.UpdateCompanyDto
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
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
            avatarId = createCompanyDto.avatarId,
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
    fun getCompany(companyId: UUID): CompanyDto? {
        val company = companyJpaRepository.findById(companyId).orElse(null)
        company ?: return null
        return mapper.toCompanyDto(company)
    }

    fun getCompanies(ids: List<UUID>): List<ShortCompanyDto> {
        return companyJpaRepository.findByIds(ids).map(mapper::toShortCompanyDto)
    }

    /**
     * Получение компании по идентификатору
     */
    fun getRawCompany(companyId: UUID): CompanyEntity? {
        val company = companyJpaRepository.findById(companyId).orElse(null)
        return company
    }

    /**
     * Получение компании по идентификатору
     */
    fun getShortCompany(companyId: UUID): ShortCompanyDto? {
        val company = companyJpaRepository.findById(companyId).orElse(null)
        company ?: return null
        return mapper.toShortCompanyDto(company)
    }

    /**
     * Получение компании по названию
     */
    fun getCompanyByName(name: String): CompanyDto? {
        val company = companyJpaRepository.findByName(name).orElse(null)
        company ?: return null
        return mapper.toCompanyDto(company)
    }

    fun findCompanyById(companyId: UUID): CompanyEntity? {
        return companyJpaRepository.findById(companyId).orElse(null)
    }

    fun updateCompany(existingCompany: CompanyEntity, updateCompanyDto: UpdateCompanyDto): CompanyDto {
        val updatedCompany = existingCompany.copy(
            name = updateCompanyDto.name ?: existingCompany.name,
            sinceYear = updateCompanyDto.sinceYear ?: existingCompany.sinceYear,
            avatarId = updateCompanyDto.avatarId ?: existingCompany.avatarId,
            agent = updateCompanyDto.agentId ?: existingCompany.agent,
            description = updateCompanyDto.description ?: existingCompany.description,
            primaryColor = updateCompanyDto.primaryColor?.toString() ?: existingCompany.primaryColor,
        )

        return mapper.toCompanyDto(companyJpaRepository.save(updatedCompany))
    }

    /**
     * Получение списка компаний
     */
    fun getCompaniesList(name: String?, lastId: UUID?, pageSize: Int): List<ShortCompanyWithEmployersDto> {
        val pageable = PageRequest.of(0, pageSize, Sort.by("createdAt").descending())

        var lastCompany: CompanyEntity? = null
        if (lastId != null) {
            lastCompany = companyJpaRepository.findById(lastId).orElse(null)
                ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Компания с ID $lastId не найдена")
        }

        val spec = Specification.where(CompanySpecification.hasNameLike(name))
            .and(CompanySpecification.createdBefore(
                lastCompany?.createdAt
            ))

        val companies = companyJpaRepository.findAll(spec, pageable).content

        return companies.map { mapper.toShortCompanyWithEmployersDto(it) }
    }

}