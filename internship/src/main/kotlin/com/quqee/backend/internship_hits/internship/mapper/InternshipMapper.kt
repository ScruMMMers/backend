package com.quqee.backend.internship_hits.internship.mapper

import com.quqee.backend.internship_hits.company.service.CompanyService
import com.quqee.backend.internship_hits.internship.entity.InternshipEntity
import com.quqee.backend.internship_hits.position.service.PositionService
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.internship_interface.InternshipDto
import com.quqee.backend.internship_hits.public_interface.profile_public.GetProfileDto
import java.util.*

class InternshipMapper(
    private val companyService: CompanyService,
    private val profileService: ProfileService,
    private val positionService: PositionService
) {
    fun toDto(userId: UUID, entity: InternshipEntity?): InternshipDto {
        val profileDto = profileService.getShortAccount(GetProfileDto(userId))

        val shortCompanyDto = entity?.let {
            runCatching { companyService.getShortCompany(it.companyId) }.getOrNull()
        }

        val positionDto = entity?.positionId?.let {
            runCatching { positionService.getPositionById(it) }.getOrNull()
        }

        return InternshipDto(
            shortAccountDto = profileDto,
            shortCompanyDto = shortCompanyDto,
            companyPositionDto = positionDto
        )
    }
}
