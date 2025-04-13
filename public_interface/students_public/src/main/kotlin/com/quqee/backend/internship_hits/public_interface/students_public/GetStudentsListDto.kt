package com.quqee.backend.internship_hits.public_interface.students_public

import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.UserId

data class GetStudentsListDto(
    val pagination: LastIdPaginationRequest<UserId>,
    val filter: GetStudentsListFilterParamDto,
)
