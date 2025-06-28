package com.quqee.backend.internship_hits.api.mapper.announcement

import com.quqee.backend.internship_hits.mapper.EnumerationMapper
import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.model.rest.ApprovalStatusEnum
import com.quqee.backend.internship_hits.model.rest.GetStudentsListFilterParamView
import com.quqee.backend.internship_hits.model.rest.LogTypeEnum
import com.quqee.backend.internship_hits.model.rest.PositionEnum
import com.quqee.backend.internship_hits.public_interface.common.enums.Position
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.students_public.GetStudentsListFilterParamDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StudentFilterMapperConfiguration(
    private val mapLogType: EnumerationMapper<LogTypeEnum, LogType>,
    private val mapApprovalStatus: EnumerationMapper<ApprovalStatusEnum, ApprovalStatus>,
    private val mapPositionType: EnumerationMapper<PositionEnum, Position>
) {

    @Bean
    fun mapStudentFilterParams(): FromApiToInternalMapper<GetStudentsListFilterParamView, GetStudentsListFilterParamDto> =
        makeFromApiMapper { view ->
            GetStudentsListFilterParamDto(
                course = view.course?.toSet(),
                group = view.group?.toSet(),
                logType = view.logType?.map { mapLogType.mapToInternal(it) }?.toSet(),
                logApprovalStatus = view.logApprovalStatus?.map { mapApprovalStatus.mapToInternal(it) }?.toSet(),
                positionType = view.positionType?.map { mapPositionType.mapToInternal(it) }?.toSet(),
                positionName = view.positionName?.toSet(),
                companyIds = view.companyIds?.toSet(),
                name = view.name
            )
        }
}
