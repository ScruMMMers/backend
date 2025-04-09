package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.meeting.service.AudienceService
import com.quqee.backend.internship_hits.meeting.service.BuildingService
import com.quqee.backend.internship_hits.meeting.service.MeetingService
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.public_interface.common.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class MeetingController(
    private val meetingService: MeetingService,
    private val buildingService: BuildingService,
    private val audienceService: AudienceService,
    private val mapBuildingsListToApi: FromInternalToApiMapper<BuildingsListView, BuildingsListDto>,
    private val mapAudiencesListToApi: FromInternalToApiMapper<AudiencesListView, AudiencesListDto>,
    private val mapCreateMeeting: FromApiToInternalMapper<CreateMeetingView, CreateMeetingDto>,
    private val mapMeetingToApi: FromInternalToApiMapper<MeetingView, MeetingDto>,
    private val mapMeetingsListToApi: FromInternalToApiMapper<MeetingsListView, MeetingsListDto>,
) : MeetingApiDelegate {

    override fun buildingsBuildingIdAudiencesGet(buildingId: UUID): ResponseEntity<AudiencesListView> {
        return ResponseEntity.ok(mapAudiencesListToApi.fromInternal(audienceService.getAudiences(buildingId)))
    }

    override fun buildingsGet(): ResponseEntity<BuildingsListView> {
        return ResponseEntity.ok(mapBuildingsListToApi.fromInternal(buildingService.getBuildings()))
    }


    override fun meetingsCompanyCompanyIdNearestGet(companyId: UUID): ResponseEntity<MeetingView> {
        return ResponseEntity.ok(mapMeetingToApi.fromInternal(meetingService.getNearestMeeting(companyId)))
    }

    override fun meetingsCompanyCompanyIdPost(
        companyId: UUID,
        createMeetingView: CreateMeetingView
    ): ResponseEntity<MeetingView> {
        val createMeetingDto = mapCreateMeeting.fromApi(createMeetingView)

        return ResponseEntity.ok(mapMeetingToApi.fromInternal(meetingService.createMeeting(companyId, createMeetingDto)))
    }

    override fun meetingsGet(
        companyId: UUID?,
        upcoming: Boolean?,
        lastId: UUID?,
        size: Int?
    ): ResponseEntity<MeetingsListView> {
        return ResponseEntity.ok(
            mapMeetingsListToApi.fromInternal(
                meetingService.getMeetings(companyId, upcoming, lastId, size)
            )
        )
    }

}