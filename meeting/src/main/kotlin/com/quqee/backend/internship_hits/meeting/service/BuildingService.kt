package com.quqee.backend.internship_hits.meeting.service

import com.quqee.backend.internship_hits.meeting.mapper.BuildingMapper
import com.quqee.backend.internship_hits.meeting.repository.BuildingRepository
import com.quqee.backend.internship_hits.meeting.specification.BuildingSpecification
import com.quqee.backend.internship_hits.public_interface.meeting.BuildingsListDto
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

interface BuildingService {
    fun getBuildings(name: String?): BuildingsListDto

}

@Service
open class BuildingServiceImpl(
    private val buildingRepository: BuildingRepository,
    private val buildingMapper: BuildingMapper,
) : BuildingService {

    override fun getBuildings(name: String?): BuildingsListDto {
        val spec = Specification.where(BuildingSpecification.byName(name))
        return BuildingsListDto(buildingRepository.findAll(spec).map { buildingMapper.mapToDto(it) })
    }

}