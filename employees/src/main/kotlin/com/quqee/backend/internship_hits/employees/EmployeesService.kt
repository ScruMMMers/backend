package com.quqee.backend.internship_hits.employees

import com.quqee.backend.internship_hits.company.service.CompanyService
import com.quqee.backend.internship_hits.employees.dto.CreateEmployeeDto
import com.quqee.backend.internship_hits.employees.dto.UpdateEmployeeDto
import com.quqee.backend.internship_hits.employees.entity.EmployeeEntity
import com.quqee.backend.internship_hits.employees.entity.EmployeesFilterParams
import com.quqee.backend.internship_hits.employees.repository.EmployeesRepository
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.profile.dto.CreateUserDto
import com.quqee.backend.internship_hits.profile.dto.UpdateUserDto
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationResponse
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.employees_public.EmployeeDto
import com.quqee.backend.internship_hits.public_interface.employees_public.GetEmployeesListDto
import com.quqee.backend.internship_hits.public_interface.profile_public.GetProfileDto
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.quqee.backend.internship_hits.public_interface.employees_public.CreateEmployeeDto as ExternalCreateEmployeeDto
import com.quqee.backend.internship_hits.public_interface.employees_public.UpdateEmployeeDto as ExternalUpdateEmployeeDto

@Service
class EmployeesService(
    private val profileService: ProfileService,
    private val companyService: CompanyService,
    private val employeesRepository: EmployeesRepository,
) {
    fun getEmployeesList(dto: GetEmployeesListDto): LastIdPaginationResponse<EmployeeDto, UserId> {
        val userIds = dto.filter.name?.let { name ->
            profileService.getUserIdsByName(name)
        }

        val filterDto = EmployeesFilterParams(
            companiesIds = dto.filter.companiesIds,
            employeesIds = userIds?.toList(),
        )
        val employees = employeesRepository.getEmployees(
            pagination = dto.pagination,
            filter = filterDto,
        )

        val employeeDtos = runBlocking {
            val deferred = employees.map { employeeEntity ->
                async {
                    mapEmployeeToDto(employeeEntity)
                }
            }
            deferred.awaitAll()
        }
        return LastIdPaginationResponse(
            employeeDtos,
            dto.pagination,
            employeesRepository.getFilteredEmployeesSize(filterDto),
        )
    }

    @Transactional
    fun createEmployee(dto: ExternalCreateEmployeeDto): EmployeeDto {
        val userId = profileService.createProfile(
            CreateUserDto(
                username = dto.email.substringBefore("@"),
                email = dto.email,
                firstName = dto.fullName.split(" ")[1],
                lastName = dto.fullName.split(" ")[0],
                password = "123",
                roles = setOf(UserRole.DEANERY),
                middleName = dto.fullName.split(" ").getOrNull(2),
                photoId = dto.photoId,
            )
        )
        val employee = employeesRepository.createEmployee(
            CreateEmployeeDto(
                userId = userId,
            )
        )
        employeesRepository.addCompaniesUser(
            UpdateEmployeeDto(
                userId = userId,
                companiesIds = dto.companyIds,
            )
        )
        return mapEmployeeToDto(employee)
    }

    @Transactional
    fun updateEmployee(dto: ExternalUpdateEmployeeDto): EmployeeDto {
        val employee = employeesRepository.getByUserId(dto.userId)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "User not found")
        val currentCompanyIds = employee.companies.map { it.companyId }
        val newCompanies = dto.companyIds.filter { it !in currentCompanyIds }
        val removedCompanies =  currentCompanyIds.filter { it !in dto.companyIds }

        employeesRepository.addCompaniesUser(
            UpdateEmployeeDto(
                dto.userId,
                newCompanies
            )
        )
        employeesRepository.deleteCompaniesUser(
            UpdateEmployeeDto(
                dto.userId,
                removedCompanies
            )
        )

        val user = profileService.getUser(dto.userId)
        val firstName = dto.fullName.split(" ")[1]
        val lastName = dto.fullName.split(" ")[0]
        val middleName = dto.fullName.split(" ").getOrNull(2)
        val updateProfileDto = user.toUpdateDto(
            email = dto.email,
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            photoId = dto.photoId,
            roles = user.roles,
        )
        profileService.updateProfile(updateProfileDto)

        return mapEmployeeToDto(employeesRepository.getByUserId(dto.userId)!!)
    }

    private fun mapEmployeeToDto(entity: EmployeeEntity): EmployeeDto {
        val profile = profileService.getShortAccount(
            GetProfileDto(userId = entity.userId)
        )
        val companies = companyService.getShortCompanies(entity.companies.map { it.companyId })
        return EmployeeDto(
            id = entity.userId,
            profile = profile,
            companies = companies
        )
    }
}