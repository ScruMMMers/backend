package com.quqee.backend.internship_hits.employees.repository

import com.quqee.backend.internship_hits.employees.dto.CreateEmployeeDto
import com.quqee.backend.internship_hits.employees.dto.UpdateEmployeeDto
import com.quqee.backend.internship_hits.employees.entity.EmployeeEntity
import com.quqee.backend.internship_hits.employees.entity.EmployeeEntity.CompanyEntity
import com.quqee.backend.internship_hits.employees.entity.EmployeeEntityMapper
import com.quqee.backend.internship_hits.employees.entity.EmployeesFilterParams
import com.quqee.backend.internship_hits.employees.public.tables.references.EMPLOYEES
import com.quqee.backend.internship_hits.employees.public.tables.references.EMPLOYEES_COMPANIES
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.SortingStrategy
import com.quqee.backend.internship_hits.public_interface.common.UserId
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Record1
import org.jooq.SelectOnConditionStep
import org.jooq.SortField
import org.springframework.stereotype.Repository
import java.time.Clock
import java.time.OffsetDateTime
import java.util.*

@Repository
class EmployeesRepository(
    private val dsl: DSLContext,
    private val clock: Clock,
) {
    fun getEmployees(
        pagination: LastIdPaginationRequest<UserId>,
        filter: EmployeesFilterParams,
    ): Collection<EmployeeEntity> {
        val filteredEmployeeIds = if (pagination.lastId != null) {
            dsl.selectStudentIdsForList()
                .where(filter.toCondition())
                .groupBy(EMPLOYEES.USER_ID)
                .orderBy(EMPLOYEES.USER_ID.asc())
                .seek(pagination.lastId!!)
                .limit(pagination.sizeForSelect)
                .fetchInto(UUID::class.java)
        } else {
            dsl.selectStudentIdsForList()
                .where(filter.toCondition())
                .groupBy(EMPLOYEES.USER_ID)
                .orderBy(EMPLOYEES.USER_ID.asc())
                .limit(pagination.sizeForSelect)
                .fetchInto(UUID::class.java)
        }

        return dsl.selectStudentForList()
            .where(EMPLOYEES.USER_ID.`in`(filteredEmployeeIds))
            .orderBy(pagination.sorting.toOrderBy())
            .fetch()
            .groupBy { it[EMPLOYEES.USER_ID]!! }
            .values
            .map(::employeeMapper)
    }

    fun getFilteredEmployeesSize(filter: EmployeesFilterParams): Int {
        return dsl.fetchCount(dsl.selectStudentIdsForList()
            .where(filter.toCondition()))
    }

    fun createEmployee(dto: CreateEmployeeDto): EmployeeEntity {
        return dsl.insertInto(EMPLOYEES)
            .set(EMPLOYEES.USER_ID, dto.userId)
            .set(EMPLOYEES.CREATED_AT, OffsetDateTime.now(clock))
            .returning(EmployeeEntity.FIELDS)
            .fetchOne(EMPLOYEE_RECORD_MAPPER)!!
    }

    fun addCompaniesUser(dto: UpdateEmployeeDto) {
        val insertStatement = dsl.insertInto(
            EMPLOYEES_COMPANIES,
            EMPLOYEES_COMPANIES.USER_ID,
            EMPLOYEES_COMPANIES.COMPANY_ID
        )
        dto.companiesIds.forEach { companyId ->
            insertStatement.values(dto.userId, companyId)
        }
        insertStatement.execute()
    }

    fun deleteCompaniesUser(dto: UpdateEmployeeDto) {
        dsl.deleteFrom(EMPLOYEES_COMPANIES)
            .where(EMPLOYEES_COMPANIES.USER_ID.eq(dto.userId))
            .and(EMPLOYEES_COMPANIES.COMPANY_ID.`in`(dto.companiesIds))
            .execute()
    }

    fun getByUserId(id: UserId): EmployeeEntity? {
        return dsl.selectStudentForList()
            .where(EMPLOYEES.USER_ID.eq(id))
            .groupBy { it[EMPLOYEES.USER_ID]!! }
            .values
            .map(::employeeMapper)
            .firstOrNull()
    }

    private fun EmployeesFilterParams.toCondition(): Collection<Condition> {
        val conditions = mutableListOf<Condition>()
        companiesIds?.let { conditions.add(EMPLOYEES_COMPANIES.COMPANY_ID.`in`(it)) }
        employeesIds?.let { conditions.add(EMPLOYEES.USER_ID.`in`(it)) }
        return conditions
    }

    private fun DSLContext.selectStudentIdsForList(): SelectOnConditionStep<Record1<UUID?>> {
        return this.selectDistinct(EMPLOYEES.USER_ID)
            .from(EMPLOYEES)
            .leftJoin(EMPLOYEES_COMPANIES)
            .on(EMPLOYEES.USER_ID.eq(EMPLOYEES_COMPANIES.USER_ID))
    }

    private fun DSLContext.selectStudentForList(): SelectOnConditionStep<Record> {
        return this.select(SELECTED_FIELDS_FOR_LIST)
            .from(EMPLOYEES)
            .leftJoin(EMPLOYEES_COMPANIES)
            .on(EMPLOYEES.USER_ID.eq(EMPLOYEES_COMPANIES.USER_ID))
    }

    private fun SortingStrategy.toOrderBy(): List<SortField<*>> {
        return when (this) {
            SortingStrategy.CREATED_AT_ASC -> listOf(EMPLOYEES.CREATED_AT.asc())
            SortingStrategy.CREATED_AT_DESC -> listOf(EMPLOYEES.CREATED_AT.desc())
        }
    }

    private fun employeeMapper(records: Collection<Record>): EmployeeEntity {
        val employeeRecord = records.first()
        return EmployeeEntity(
            userId = employeeRecord[EMPLOYEES.USER_ID]!!,
            createdAt = employeeRecord[EMPLOYEES.CREATED_AT]!!,
            companies = records.mapNotNull { record ->
                if (record[EMPLOYEES_COMPANIES.COMPANY_ID] == null) {
                    null
                } else {
                    CompanyEntity(
                        companyId = record[EMPLOYEES_COMPANIES.COMPANY_ID]!!
                    )
                }
            }.toSet()
        )
    }

    companion object {
        private val EMPLOYEE_RECORD_MAPPER = EmployeeEntityMapper()
        private val SELECTED_FIELDS_FOR_LIST = listOf(
            EMPLOYEES.USER_ID,
            EMPLOYEES.CREATED_AT,
            EMPLOYEES_COMPANIES.COMPANY_ID,
        )
    }
}