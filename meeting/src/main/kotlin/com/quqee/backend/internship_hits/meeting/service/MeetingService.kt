package com.quqee.backend.internship_hits.meeting.service

import com.quqee.backend.internship_hits.meeting.entity.MeetingEntity
import com.quqee.backend.internship_hits.meeting.mapper.MeetingMapper
import com.quqee.backend.internship_hits.meeting.repository.MeetingRepository
import com.quqee.backend.internship_hits.meeting.specification.MeetingSpecification
import com.quqee.backend.internship_hits.public_interface.common.*
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.meeting.enums.MeetingTypeEnum
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.meeting.*
import org.springframework.beans.support.PagedListHolder
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

interface MeetingService {
    fun createMeeting(companyId: UUID, createMeetingDto: CreateMeetingDto): MeetingDto

    fun getNearestMeeting(companyId: UUID): MeetingsListDto

    fun getMeetings(companyId: UUID?, upcoming: Boolean?, lastId: UUID?, size: Int?): MeetingsListPageableDto

    fun updateMeeting(meetingId: UUID, updateMeetingDto: UpdateMeetingDto): MeetingDto

    fun deleteMeeting(meetingId: UUID)

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
                createMeetingDto.date.withNano(kotlin.random.Random.nextInt(0, 999_999_999)),
                createMeetingDto.place,
                createMeetingDto.meetingType,
                companyId
            )
        )

        return meetingMapper.mapToDto(meetingEntity)
    }

    override fun getNearestMeeting(companyId: UUID): MeetingsListDto {
        val entity = meetingRepository.findNearestMeetingByCompany(companyId).orElse(null)
            ?: return meetingMapper.mapToListDto(emptyList())

        return meetingMapper.mapToListDto(listOf(meetingMapper.mapToDto(entity)))
    }

    override fun getMeetings(companyId: UUID?, upcoming: Boolean?, lastId: UUID?, size: Int?): MeetingsListPageableDto {
        val pageSize = size ?: PagedListHolder.DEFAULT_PAGE_SIZE
        val sort =
            if (upcoming == true) Sort.by("date").ascending() else Sort.by("date").descending()
        val pageable = PageRequest.of(
            0,
            pageSize + 1,
            sort
        )

        var lastMeeting: MeetingEntity? = null
        if (lastId != null) {
            lastMeeting = meetingRepository.findById(lastId).orElse(null)
                ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Встреча с ID $lastId не найдена")
        }

        val spec = Specification.where(MeetingSpecification.byCompanyId(companyId))
            .and(MeetingSpecification.byUpcoming(upcoming))
            .and(MeetingSpecification.byDateAt(lastMeeting?.date, upcoming ?: true))

        val meetings = meetingRepository.findAll(spec, pageable).content.map { meetingMapper.mapToWithAgentDto(it) }
        val hasNext = meetings.size > pageSize

        return MeetingsListPageableDto(
            meetings = meetings.take(pageSize),
            page = LastIdPagination(
                lastId = if (meetings.isNotEmpty()) meetings.take(pageSize).last().id else null,
                pageSize = pageSize,
                hasNext = hasNext
            )
        )
    }

    override fun updateMeeting(meetingId: UUID, updateMeetingDto: UpdateMeetingDto): MeetingDto {
        val meeting = meetingRepository.findById(meetingId).orElseThrow {
            ExceptionInApplication(ExceptionType.NOT_FOUND, "Встреча с ID $meetingId не найдена")
        }

        val newMeeting = meeting.copy(
            date = updateMeetingDto.date.withNano(kotlin.random.Random.nextInt(0, 999_999_999)),
            place = updateMeetingDto.place,
            meetingType = updateMeetingDto.meetingType
        )

        return meetingMapper.mapToDto(meetingRepository.save(newMeeting))
    }

    override fun deleteMeeting(meetingId: UUID) {
        val meeting = meetingRepository.findById(meetingId).orElseThrow {
            ExceptionInApplication(ExceptionType.NOT_FOUND, "Встреча с ID $meetingId не найдена")
        }

        meetingRepository.delete(meeting)
    }


}