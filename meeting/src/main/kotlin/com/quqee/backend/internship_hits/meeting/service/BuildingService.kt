package com.quqee.backend.internship_hits.meeting.service

import com.quqee.backend.internship_hits.meeting.mapper.BuildingMapper
import com.quqee.backend.internship_hits.meeting.repository.BuildingRepository
import com.quqee.backend.internship_hits.public_interface.common.BuildingsListDto
import org.springframework.stereotype.Service

interface BuildingService {
    fun getBuildings(): BuildingsListDto

}

@Service
open class BuildingServiceImpl(
    private val buildingRepository: BuildingRepository,
    private val buildingMapper: BuildingMapper,
) : BuildingService {

    override fun getBuildings(): BuildingsListDto {
        return BuildingsListDto(buildingRepository.findAll().map { buildingMapper.mapToDto(it) })
    }

}