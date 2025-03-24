package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.company.service.CompanyPositionService
import com.quqee.backend.internship_hits.company.service.CompanyService
import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.public_interface.company.CompaniesListDto
import com.quqee.backend.internship_hits.public_interface.company.CompanyDto
import com.quqee.backend.internship_hits.public_interface.company.CreateCompanyDto
import com.quqee.backend.internship_hits.public_interface.company_position.CompanyPositionDto
import com.quqee.backend.internship_hits.public_interface.company_position.CreateCompanyPositionDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class CompanyController(
    private val companyService: CompanyService,
    private val companyPositionService: CompanyPositionService,
    private val mapCompany: FromInternalToApiMapper<CompanyView, CompanyDto>,
    private val mapCompanyPosition: FromInternalToApiMapper<CompanyPositionView, CompanyPositionDto>,
    private val mapCompaniesList: FromInternalToApiMapper<CompaniesListView, CompaniesListDto>,
    private val mapCreateCompany: FromApiToInternalMapper<CreateCompanyView, CreateCompanyDto>,
    private val mapCreateCompanyPosition: FromApiToInternalMapper<CreateCompanyPositionView, CreateCompanyPositionDto>
) : CompanyApiDelegate {

    override fun companyCompanyIdGet(companyId: UUID): ResponseEntity<CompanyView> {
        return ResponseEntity.ok(mapCompany.fromInternal(companyService.getCompany(companyId)))
    }

    override fun companyListGet(name: String?, lastId: UUID?, size: Int?): ResponseEntity<CompaniesListView> {
        return ResponseEntity.ok(mapCompaniesList.fromInternal(companyService.getCompaniesList(name, lastId, size)))
    }

    override fun companyPost(createCompanyView: CreateCompanyView): ResponseEntity<CompanyView> {
        val createCompanyDto = mapCreateCompany.fromApi(createCompanyView)

        return ResponseEntity.ok(mapCompany.fromInternal(companyService.createCompany(createCompanyDto)))
    }

    override fun companyCompanyIdPositionPost(
        createCompanyPositionView: CreateCompanyPositionView
    ): ResponseEntity<CompanyPositionView> {
        val createCompanyPositionDto = mapCreateCompanyPosition.fromApi(createCompanyPositionView)

        return ResponseEntity.ok(
            mapCompanyPosition.fromInternal(
                companyPositionService.createCompanyPosition(createCompanyPositionDto)
            )
        )
    }

}