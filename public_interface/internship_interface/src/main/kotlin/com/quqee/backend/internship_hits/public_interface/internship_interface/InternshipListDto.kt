package com.quqee.backend.internship_hits.public_interface.internship_interface

import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination

data class InternshipListDto(
    val internships: List<InternshipDto>,
    val page: LastIdPagination
)
