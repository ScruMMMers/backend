package com.quqee.backend.internship_hits.api.mapper.semester

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.SemesterView
import com.quqee.backend.internship_hits.public_interface.semester_interface.SemesterDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SemesterConfigurationMapper {

    @Bean
    fun mapSemester(): FromApiToInternalMapper<SemesterView, SemesterDto> =
        makeFromApiMapper { model ->
            SemesterDto(
                id = model.id,
                name = model.name,
                startDate = model.startDate,
                endDate = model.endDate,
                practiceDiaryStart = model.practiceDiaryStart,
                practiceDiaryEnd = model.practiceDiaryEnd,
                companyChangeStart = model.companyChangeStart,
                companyChangeEnd = model.companyChangeEnd
            )
        }

    @Bean
    fun mapSemesterToApi(): FromInternalToApiMapper<SemesterView, SemesterDto> =
        makeToApiMapper { model ->
            SemesterView(
                id = model.id,
                name = model.name,
                startDate = model.startDate,
                endDate = model.endDate,
                practiceDiaryStart = model.practiceDiaryStart,
                practiceDiaryEnd = model.practiceDiaryEnd,
                companyChangeStart = model.companyChangeStart,
                companyChangeEnd = model.companyChangeEnd
            )
        }
}
