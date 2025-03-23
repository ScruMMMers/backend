package com.quqee.backend.internship_hits.company.service

import com.quqee.backend.internship_hits.company.repository.CompanyRepository
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import com.quqee.backend.internship_hits.public_interface.company.CompanyDto
import com.quqee.backend.internship_hits.public_interface.company.CompaniesListDto
import com.quqee.backend.internship_hits.public_interface.company.CreateCompanyDto
import org.springframework.stereotype.Service
import java.util.UUID

interface CompanyService {
    fun createCompany(createCompanyView: CreateCompanyDto): CompanyDto
    fun getCompany(companyId: UUID): CompanyDto
    fun getCompaniesList(name: String?, lastId: Int?, size: Int?): CompaniesListDto
}

@Service
class CompanyServiceImpl(
    private val companyRepository: CompanyRepository
) : CompanyService {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }

    /**
     * Создание компании
     */
    override fun createCompany(createCompanyView: CreateCompanyDto): CompanyDto {
        //TODO валидация
        return companyRepository.createCompany(createCompanyView)
    }

    /**
     * Получение компании по идентификатору
     */
    override fun getCompany(companyId: UUID): CompanyDto {
        //TODO обработка ошибки
        return companyRepository.getCompany(companyId)
    }

    /**
     * Получение списка компаний
     */
    override fun getCompaniesList(name: String?, lastId: Int?, size: Int?): CompaniesListDto {
        val pageSize = size ?: DEFAULT_PAGE_SIZE
        val companies = companyRepository.getCompaniesList(name, lastId, pageSize)
        val hasNext = companies.size >= pageSize

        return CompaniesListDto(
            companies = companies,
            page = LastIdPagination(
                lastId = 10.toString(),
                pageSize = pageSize,
                hasNext = hasNext
            )
        )
    }
}