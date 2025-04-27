package com.quqee.backend.internship_hits.public_interface.announcement_interface

import com.quqee.backend.internship_hits.public_interface.students_public.GetStudentsListFilterParamDto

data class CreateAnnouncementByFilterDto(
    val data: AnnouncementDataDto,
    val filter: GetStudentsListFilterParamDto
)