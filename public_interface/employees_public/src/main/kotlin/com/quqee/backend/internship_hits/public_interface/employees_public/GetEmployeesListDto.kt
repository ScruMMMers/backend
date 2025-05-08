package com.quqee.backend.internship_hits.public_interface.employees_public

import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.UserId

data class GetEmployeesListDto(
    val pagination: LastIdPaginationRequest<UserId>,
    val filter: GetEmployeesListFilterParamDto,
)
