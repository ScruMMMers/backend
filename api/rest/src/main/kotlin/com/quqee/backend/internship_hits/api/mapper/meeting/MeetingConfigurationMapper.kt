package com.quqee.backend.internship_hits.api.mapper.meeting

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.public_interface.common.*
import com.quqee.backend.internship_hits.public_interface.common.enums.MeetingTypeEnum
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MeetingConfigurationMapper {

    @Bean
    fun mapMeeting(
        shortCompanyMapper: FromApiToInternalMapper<ShortCompanyView, ShortCompanyDto>
    ): FromApiToInternalMapper<MeetingView, MeetingDto> = makeFromApiMapper { model ->
        MeetingDto(
            id = model.id,
            date = model.date,
            place = model.place,
            meetingType = MeetingTypeEnum.valueOf(model.meetingType.value),
            company = shortCompanyMapper.fromApi(model.company)
        )
    }

    @Bean
    fun mapMeetingToApi(
        shortCompanyMapper: FromInternalToApiMapper<ShortCompanyView, ShortCompanyDto>
    ): FromInternalToApiMapper<MeetingView, MeetingDto> = makeToApiMapper { model ->
        MeetingView(
            id = model.id,
            date = model.date,
            place = model.place,
            meetingType = com.quqee.backend.internship_hits.model.rest.MeetingTypeEnum.valueOf(model.meetingType.name),
            company = shortCompanyMapper.fromInternal(model.company)
        )
    }

    @Bean
    fun mapCreateMeeting(
    ): FromApiToInternalMapper<CreateMeetingView, CreateMeetingDto> = makeFromApiMapper { model ->
        CreateMeetingDto(
            date = model.date,
            place = model.place,
            meetingType = MeetingTypeEnum.valueOf(model.meetingType.value)
        )
    }

    @Bean
    fun mapUpdateMeeting(
    ): FromApiToInternalMapper<UpdateMeetingView, UpdateMeetingDto> = makeFromApiMapper { model ->
        UpdateMeetingDto(
            date = model.date,
            place = model.place,
            meetingType = MeetingTypeEnum.valueOf(model.meetingType.value)
        )
    }

    @Bean
    fun mapMeetingsList(
        mapMeetingToApi: FromInternalToApiMapper<MeetingView, MeetingDto>,
        mapPage: FromInternalToApiMapper<LastIdPaginationView, LastIdPagination>
    ): FromInternalToApiMapper<MeetingsListView, MeetingsListDto> = makeToApiMapper { model ->
        MeetingsListView(
            meetings = model.meetings.map { mapMeetingToApi.fromInternal(it) }
        )
    }

    @Bean
    fun mapMeetingsListPageable(
        mapMeetingToApi: FromInternalToApiMapper<MeetingView, MeetingDto>,
        mapPage: FromInternalToApiMapper<LastIdPaginationView, LastIdPagination>
    ): FromInternalToApiMapper<MeetingsListPageableView, MeetingsListPageableDto> = makeToApiMapper { model ->
        MeetingsListPageableView(
            meetings = model.meetings.map { mapMeetingToApi.fromInternal(it) },
            page = mapPage.fromInternal(model.page)
        )
    }
}