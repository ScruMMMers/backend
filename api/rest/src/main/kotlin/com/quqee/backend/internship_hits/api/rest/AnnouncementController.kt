package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.announcement.AnnouncementService
import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.model.rest.AnnouncementDataView
import com.quqee.backend.internship_hits.model.rest.CreateAnnouncementByFilterView
import com.quqee.backend.internship_hits.model.rest.CreateAnnouncementView
import com.quqee.backend.internship_hits.model.rest.GetStudentsListFilterParamView
import com.quqee.backend.internship_hits.public_interface.announcement_interface.AnnouncementDataDto
import com.quqee.backend.internship_hits.public_interface.announcement_interface.CreateAnnouncementByFilterDto
import com.quqee.backend.internship_hits.public_interface.announcement_interface.CreateAnnouncementDto
import com.quqee.backend.internship_hits.public_interface.students_public.GetStudentsListFilterParamDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class AnnouncementController(
    private val announcementService: AnnouncementService,
    private val mapAnnouncementData: FromApiToInternalMapper<AnnouncementDataView, AnnouncementDataDto>,
    private val mapStudentFilter: FromApiToInternalMapper<GetStudentsListFilterParamView, GetStudentsListFilterParamDto>
) : AnnouncementApiDelegate {
    override fun announcementFilterPost(createAnnouncementByFilterView: CreateAnnouncementByFilterView): ResponseEntity<Unit> {
        announcementService.sendAnnouncementByFilter(
            CreateAnnouncementByFilterDto(
                data = mapAnnouncementData.fromApi(createAnnouncementByFilterView.data),
                filter = mapStudentFilter.fromApi(createAnnouncementByFilterView.filter),
                excludeUserIds = createAnnouncementByFilterView.filter.excludeUserIds
            )
        )
        return ResponseEntity.ok().build()
    }

    override fun announcementPost(createAnnouncementView: CreateAnnouncementView): ResponseEntity<Unit> {
        announcementService.sendAnnouncementByUserId(
            CreateAnnouncementDto(
                data = mapAnnouncementData.fromApi(createAnnouncementView.data),
                userIds = createAnnouncementView.userIds
            )
        )
        return ResponseEntity.ok().build()
    }
}