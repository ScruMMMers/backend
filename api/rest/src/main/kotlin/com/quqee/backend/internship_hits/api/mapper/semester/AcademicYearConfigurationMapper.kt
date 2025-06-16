package com.quqee.backend.internship_hits.api.mapper.semester

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.AcademicYearView
import com.quqee.backend.internship_hits.model.rest.SemesterView
import com.quqee.backend.internship_hits.public_interface.semester_interface.AcademicYearDto
import com.quqee.backend.internship_hits.public_interface.semester_interface.SemesterDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AcademicYearConfigurationMapper(
    private val mapSemesterInternal: FromInternalToApiMapper<SemesterView, SemesterDto>,
    private val mapSemesterApi: FromApiToInternalMapper<SemesterView, SemesterDto>,
) {

    @Bean
    fun mapAcademicYear(): FromApiToInternalMapper<AcademicYearView, AcademicYearDto> =
        makeFromApiMapper { model ->
            AcademicYearDto(
                id = model.id,
                yearRange = model.yearRange,
                semesters = model.semesters.map { mapSemesterApi.fromApi(it) },
            )
        }

    @Bean
    fun mapAcademicYearToApi(): FromInternalToApiMapper<AcademicYearView, AcademicYearDto> =
        makeToApiMapper { model ->
            AcademicYearView(
                id = model.id,
                yearRange = model.yearRange,
                semesters = model.semesters.map { semesterDto -> mapSemesterInternal.fromInternal(semesterDto) }
            )
        }
}
