package com.quqee.backend.internship_hits.meeting.service

import com.quqee.backend.internship_hits.meeting.mapper.AudienceMapper
import com.quqee.backend.internship_hits.meeting.repository.AudienceRepository
import com.quqee.backend.internship_hits.meeting.specification.AudienceSpecification
import com.quqee.backend.internship_hits.public_interface.meeting.AudiencesListDto
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*

interface AudienceService {
    fun getAudiences(buildingId: UUID, name: String?): AudiencesListDto

}

@Service
open class AudienceServiceImpl(
    private val audienceRepository: AudienceRepository,
    private val audienceMapper: AudienceMapper,
) : AudienceService {

    override fun getAudiences(buildingId: UUID, name: String?): AudiencesListDto {
        val spec = Specification.where(AudienceSpecification.byName(name))
            .and(AudienceSpecification.byBuildingId(buildingId))

        return AudiencesListDto(audienceRepository.findAll(spec).map { audienceMapper.mapToDto(it) })
    }

}