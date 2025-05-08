package com.quqee.backend.internship_hits.employees.entity

import com.quqee.backend.internship_hits.employees.public.tables.records.EmployeesRecord
import com.quqee.backend.internship_hits.employees.public.tables.references.EMPLOYEES
import org.jooq.RecordMapper

class EmployeeEntityMapper : RecordMapper<EmployeesRecord, EmployeeEntity> {
    override fun map(record: EmployeesRecord): EmployeeEntity {
        return EmployeeEntity(
            userId = record[EMPLOYEES.USER_ID]!!,
            createdAt = record[EMPLOYEES.CREATED_AT]!!,
            companies = emptySet(),
        )
    }
}