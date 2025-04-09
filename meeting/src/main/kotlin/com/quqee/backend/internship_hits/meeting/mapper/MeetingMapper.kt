package com.quqee.backend.internship_hits.meeting.mapper

import com.quqee.backend.internship_hits.company.mapper.CompanyMapper
import com.quqee.backend.internship_hits.company.service.CompanyService
import com.quqee.backend.internship_hits.meeting.entity.MeetingEntity
import com.quqee.backend.internship_hits.public_interface.common.MeetingDto
import org.springframework.stereotype.Component

@Component
class MeetingMapper(
    private val companyMapper: CompanyMapper,
    private val companyService: CompanyService,
) {

    fun mapToDto(meetingEntity: MeetingEntity): MeetingDto {
        return MeetingDto(
            meetingEntity.id,
            meetingEntity.date,
            meetingEntity.place,
            meetingEntity.meetingType,
            companyMapper.toShortCompanyDto(companyService.getRawCompany(meetingEntity.companyId))
        )
    }

} 