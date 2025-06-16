package com.quqee.backend.internship_hits.api.mapper.semester

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.UpdateSemesterView
import com.quqee.backend.internship_hits.public_interface.semester_interface.UpdateSemesterDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UpdateSemesterConfigurationMapper {

    @Bean
    fun mapUpdateSemester(): FromApiToInternalMapper<UpdateSemesterView, UpdateSemesterDto> =
        makeFromApiMapper { model ->
            UpdateSemesterDto(
                startDate = model.startDate,
                endDate = model.endDate,
                practiceDiaryStart = model.practiceDiaryStart,
                practiceDiaryEnd = model.practiceDiaryEnd,
                companyChangeStart = model.companyChangeStart,
                companyChangeEnd = model.companyChangeEnd
            )
        }

    @Bean
    fun mapUpdateSemesterToApi(): FromInternalToApiMapper<UpdateSemesterView, UpdateSemesterDto> =
        makeToApiMapper { model ->
            UpdateSemesterView(
                startDate = model.startDate,
                endDate = model.endDate,
                practiceDiaryStart = model.practiceDiaryStart,
                practiceDiaryEnd = model.practiceDiaryEnd,
                companyChangeStart = model.companyChangeStart,
                companyChangeEnd = model.companyChangeEnd
            )
        }
}
