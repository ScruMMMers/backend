package com.quqee.backend.internship_hits.meeting.repository

import com.quqee.backend.internship_hits.meeting.entity.AudienceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AudienceRepository : JpaRepository<AudienceEntity, UUID> {

    fun findAllByBuildingId(buildingId: UUID): List<AudienceEntity>

}