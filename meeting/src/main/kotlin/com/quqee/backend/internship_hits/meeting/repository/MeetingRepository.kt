package com.quqee.backend.internship_hits.meeting.repository

import com.quqee.backend.internship_hits.meeting.entity.MeetingEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MeetingRepository : JpaRepository<MeetingEntity, UUID>, JpaSpecificationExecutor<MeetingEntity> {

    @Query(
        nativeQuery = true, value = """
            SELECT * FROM meeting m
            WHERE m.company_id = :companyId
            AND m.date > NOW()
            ORDER BY m.date ASC
            LIMIT 1
        """
    )
    fun findNearestMeetingByCompany(companyId: UUID): Optional<MeetingEntity>

}