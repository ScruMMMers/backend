package com.quqee.backend.internship_hits.public_interface.internship_interface

import com.quqee.backend.internship_hits.public_interface.common.PositionDto
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.profile_public.ProfileDto

data class InternshipDto(
    val shortAccountDto: ShortAccountDto,
    val shortCompanyDto: ShortCompanyDto?,
    val companyPositionDto: PositionDto?
)