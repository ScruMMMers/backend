package com.quqee.backend.internship_hits.meeting.service

import com.quqee.backend.internship_hits.meeting.entity.MeetingEntity
import com.quqee.backend.internship_hits.meeting.mapper.MeetingMapper
import com.quqee.backend.internship_hits.meeting.repository.MeetingRepository
import com.quqee.backend.internship_hits.meeting.specification.MeetingSpecification
import com.quqee.backend.internship_hits.public_interface.common.CreateMeetingDto
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import com.quqee.backend.internship_hits.public_interface.common.MeetingDto
import com.quqee.backend.internship_hits.public_interface.common.MeetingsListDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.enums.MeetingTypeEnum
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import org.springframework.beans.support.PagedListHolder
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

interface MeetingService {
    fun createMeeting(companyId: UUID, createMeetingDto: CreateMeetingDto): MeetingDto

    fun getNearestMeeting(companyId: UUID): MeetingDto

    fun getMeetings(companyId: UUID?, upcoming: Boolean?, lastId: UUID?, size: Int?): MeetingsListDto

}

@Service
open class MeetingServiceImpl(
    private val meetingRepository: MeetingRepository,
    private val meetingMapper: MeetingMapper,
) : MeetingService {

    override fun createMeeting(companyId: UUID, createMeetingDto: CreateMeetingDto): MeetingDto {

        if (createMeetingDto.place == null && createMeetingDto.meetingType != MeetingTypeEnum.ONLINE) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Не указано место встречи")
        }

        if (createMeetingDto.date.isBefore(OffsetDateTime.now())) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Дата встречи не может быть в прошлом")
        }

        val meetingEntity = meetingRepository.save(
            MeetingEntity(
                UUID.randomUUID(),
                createMeetingDto.date,
                createMeetingDto.place,
                createMeetingDto.meetingType,
                companyId
            )
        )

        return meetingMapper.mapToDto(meetingEntity)
    }

    override fun getNearestMeeting(companyId: UUID): MeetingDto {
        val entity = meetingRepository.findNearestMeetingByCompany(companyId).orElseThrow {
            ExceptionInApplication(ExceptionType.NOT_FOUND)
        }

        return meetingMapper.mapToDto(entity)
    }

    override fun getMeetings(companyId: UUID?, upcoming: Boolean?, lastId: UUID?, size: Int?): MeetingsListDto {
        val pageSize = size ?: PagedListHolder.DEFAULT_PAGE_SIZE
        val sort =
            if (upcoming == false) Sort.by("date").descending() else Sort.by("date").ascending()
        val pageable = PageRequest.of(
            0,
            pageSize,
            sort
        )

        var lastMeeting: MeetingEntity? = null
        if (lastId != null) {
            lastMeeting = meetingRepository.findById(lastId).orElse(null)
                ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Встреча с ID $lastId не найдена")
        }

        val spec = Specification.where(MeetingSpecification.byCompanyId(companyId))
            .and(MeetingSpecification.byUpcoming(upcoming ?: true))
            .and(MeetingSpecification.byDateAt(lastMeeting?.date, upcoming ?: false))

        val meetings = meetingRepository.findAll(spec, pageable).content.map { meetingMapper.mapToDto(it) }
        val hasNext = meetings.size >= pageSize

        return MeetingsListDto(
            meetings = meetings,
            page = LastIdPagination(
                lastId = if (meetings.isNotEmpty()) meetings.last().id else null,
                pageSize = pageSize,
                hasNext = hasNext
            )
        )
    }


}