package com.quqee.backend.internship_hits.meeting.mapper

import com.quqee.backend.internship_hits.company.mapper.CompanyMapper
import com.quqee.backend.internship_hits.company.service.CompanyService
import com.quqee.backend.internship_hits.meeting.entity.MeetingEntity
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.GetProfileDto
import com.quqee.backend.internship_hits.public_interface.meeting.MeetingDto
import com.quqee.backend.internship_hits.public_interface.meeting.MeetingWithAgentDto
import com.quqee.backend.internship_hits.public_interface.meeting.MeetingsListDto
import org.springframework.stereotype.Component

@Component
class MeetingMapper(
    private val companyMapper: CompanyMapper,
    private val companyService: CompanyService,
    private val profileService: ProfileService,
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

    fun mapToWithAgentDto(meetingEntity: MeetingEntity): MeetingWithAgentDto {
        val company = companyService.getRawCompany(meetingEntity.companyId)
        return MeetingWithAgentDto(
            meetingEntity.id,
            meetingEntity.date,
            meetingEntity.place,
            meetingEntity.meetingType,
            companyMapper.toShortCompanyDto(company),
            company.employees.firstOrNull()?.let { profileService.getShortAccount(GetProfileDto(it)) },
        )
    }

    fun mapToListDto(list: List<MeetingDto>): MeetingsListDto {
        return MeetingsListDto(
            list
        )
    }

} 