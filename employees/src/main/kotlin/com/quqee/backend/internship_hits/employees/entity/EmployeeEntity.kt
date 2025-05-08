package com.quqee.backend.internship_hits.employees.entity

import com.quqee.backend.internship_hits.employees.public.tables.references.EMPLOYEES
import com.quqee.backend.internship_hits.public_interface.common.UserId
import java.time.OffsetDateTime
import java.util.UUID

data class EmployeeEntity(
    val userId: UserId,
    val createdAt: OffsetDateTime,
    val companies: Set<CompanyEntity>,
) {
    data class CompanyEntity(
        val companyId: UUID,
    )

    companion object {
        val FIELDS = listOf(
            EMPLOYEES.USER_ID,
            EMPLOYEES.CREATED_AT,
        )
    }
}
