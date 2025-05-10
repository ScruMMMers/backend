package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.employees.EmployeesService
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationResponse
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.employees_public.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class EmployeesController(
    private val employeesService: EmployeesService,
    private val employeeViewMapper: FromInternalToApiMapper<EmployeeView, EmployeeDto>,
    private val lastIdPaginationResponseMapper: FromInternalToApiMapper<LastIdPaginationView, LastIdPaginationResponse<*, UUID>>,
    ) : EmployeesApiDelegate {
    override fun employeesCreatePost(createEmployeeView: CreateEmployeeView): ResponseEntity<EmployeeView> {
        val dto = CreateEmployeeDto(
            fullName = createEmployeeView.fullName,
            email = createEmployeeView.email,
            photoId = createEmployeeView.avatarId?.toString()
        )
        val employee = employeesService.createEmployee(dto)

        return ResponseEntity.ok(employeeViewMapper.fromInternal(employee))
    }

    override fun employeesUpdatePost(updateEmployeeView: UpdateEmployeeView): ResponseEntity<EmployeeView> {
        val userId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.FORBIDDEN)
        val dto = UpdateEmployeeDto(
            userId = userId,
            companyIds = updateEmployeeView.companyIds ?: emptyList(),
            fullName = updateEmployeeView.fullName,
            photoId = updateEmployeeView.avatarId?.toString(),
            email = updateEmployeeView.email,
        )
        val employee = employeesService.updateEmployee(dto)

        return ResponseEntity.ok(employeeViewMapper.fromInternal(employee))
    }

    override fun employeesListGet(
        lastId: UUID?,
        size: Int?,
        companyIds: List<UUID>?,
        userIds: List<UUID>?
    ): ResponseEntity<GetEmployeesListResponseView> {
        val dto = GetEmployeesListDto(
            pagination = LastIdPaginationRequest(
                lastId = lastId,
                pageSize = size,
            ),
            filter = GetEmployeesListFilterParamDto(
                companiesIds = companyIds,
                employeesIds = userIds,
            )
        )
        val employees = employeesService.getEmployeesList(dto)
        return ResponseEntity.ok(
            GetEmployeesListResponseView(
                employees = employees.responseCollection.map { employeeViewMapper.fromInternal(it) },
                page = lastIdPaginationResponseMapper.fromInternal(employees),
            )
        )
    }
}