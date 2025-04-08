package com.quqee.backend.internship_hits.meeting.service

import com.quqee.backend.internship_hits.meeting.mapper.AudienceMapper
import com.quqee.backend.internship_hits.meeting.repository.AudienceRepository
import com.quqee.backend.internship_hits.public_interface.common.AudienceDto
import com.quqee.backend.internship_hits.public_interface.common.AudiencesListDto
import org.springframework.stereotype.Service
import java.util.*

interface AudienceService {
    fun getAudiences(buildingId: UUID): AudiencesListDto

}

@Service
open class AudienceServiceImpl(
    private val audienceRepository: AudienceRepository,
    private val audienceMapper: AudienceMapper,
) : AudienceService {

    override fun getAudiences(buildingId: UUID): AudiencesListDto {
        return AudiencesListDto(audienceRepository.findAllByBuildingId(buildingId).map { audienceMapper.mapToDto(it) })
    }

}