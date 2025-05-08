package com.quqee.backend.internship_hits.company.service

import com.quqee.backend.internship_hits.company.entity.CompanyEntity
import com.quqee.backend.internship_hits.company.repository.CompanyRepository
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.company.CompaniesListDto
import com.quqee.backend.internship_hits.public_interface.company.CompanyDto
import com.quqee.backend.internship_hits.public_interface.company.CreateCompanyDto
import com.quqee.backend.internship_hits.public_interface.company.UpdateCompanyDto
import com.quqee.backend.internship_hits.tags.service.TagService
import org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Year
import java.util.*

interface CompanyService {
    fun createCompany(createCompanyDto: CreateCompanyDto): CompanyDto
    fun updateCompany(companyId: UUID, updateCompanyDto: UpdateCompanyDto): CompanyDto
    fun getCompany(companyId: UUID): CompanyDto
    fun getRawCompany(companyId: UUID): CompanyEntity
    fun getShortCompany(companyId: UUID): ShortCompanyDto?
    fun getCompaniesList(name: String?, lastId: UUID?, size: Int?): CompaniesListDto
}

@Service
open class CompanyServiceImpl(
    private val companyRepository: CompanyRepository,
    private val tagService: TagService
) : CompanyService {

    /**
     * Создание компании
     */
    @Transactional
    override fun createCompany(createCompanyDto: CreateCompanyDto): CompanyDto {
        checkYearValidation(createCompanyDto.sinceYear)

        val existingCompany: CompanyDto? = companyRepository.getCompanyByName(createCompanyDto.name)
        existingCompany?.let {
            throw ExceptionInApplication(
                ExceptionType.CONFLICT,
                "Компания с названием ${existingCompany.name} уже существует"
            )
        }

        val company = companyRepository.createCompany(createCompanyDto)

        tagService.createTag(company.name, company.companyId)

        return company
    }

    @Transactional
    override fun updateCompany(companyId: UUID, updateCompanyDto: UpdateCompanyDto): CompanyDto {
        updateCompanyDto.sinceYear?.let { checkYearValidation(updateCompanyDto.sinceYear!!) }

        val existingCompany: CompanyEntity = companyRepository.findCompanyById(companyId)
            ?: throw ExceptionInApplication(
                ExceptionType.NOT_FOUND,
                "Компания с идентификатором $companyId не найдена"
            )

        val company = companyRepository.updateCompany(existingCompany, updateCompanyDto)

        tagService.updateTagByCompanyId(companyId, company.name)

        return company
    }

    /**
     * Получение компании по идентификатору
     */
    @Transactional(readOnly = true)
    override fun getCompany(companyId: UUID): CompanyDto {
        val company = companyRepository.getCompany(companyId)
        company ?: throw ExceptionInApplication(
            ExceptionType.NOT_FOUND,
            "Компания не найдена по идентификатору $companyId"
        )
        return company
    }

    override fun getRawCompany(companyId: UUID): CompanyEntity {
        val company = companyRepository.getRawCompany(companyId)
        company ?: throw ExceptionInApplication(
            ExceptionType.NOT_FOUND,
            "Компания не найдена по идентификатору $companyId"
        )
        return company
    }

    @Transactional(readOnly = true)
    override fun getShortCompany(companyId: UUID): ShortCompanyDto? {
        return companyRepository.getShortCompany(companyId)
    }

    /**
     * Получение списка компаний
     */
    @Transactional(readOnly = true)
    override fun getCompaniesList(name: String?, lastId: UUID?, size: Int?): CompaniesListDto {
        val pageSize = size ?: DEFAULT_PAGE_SIZE
        val companies = companyRepository.getCompaniesList(name, lastId, pageSize)
        val hasNext = companies.size >= pageSize

        return CompaniesListDto(
            companies = companies,
            page = LastIdPagination(
                lastId = if (companies.isNotEmpty()) companies.last().companyId else null,
                pageSize = pageSize,
                hasNext = hasNext
            )
        )
    }

    private fun checkYearValidation(year: String) {
        val currentYear = Year.now().value
        require(year.matches(Regex("^\\d{4}$"))) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Год должен состоять из 4 цифр")
        }
        require(year.toInt() in 1000..currentYear) {
            throw ExceptionInApplication(
                ExceptionType.BAD_REQUEST,
                "Год не может быть меньше 1000 и не может быть в будущем"
            )
        }
    }
}