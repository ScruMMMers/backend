package com.quqee.backend.internship_hits.api.mapper.mark

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.public_interface.common.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class StudentsMarksListConfigurationMapper(
    private val mapStudentsMarks: FromInternalToApiMapper<StudentsMarksView, StudentsMarksDto>,
    private val mapPagination: FromInternalToApiMapper<LastIdPaginationView, LastIdPagination>,
) {
    @Bean
    open fun mapStudentsMarksListToApi(

    ): FromInternalToApiMapper<StudentsMarksListView, StudentsMarksListDto> = makeToApiMapper { model ->
        StudentsMarksListView(
            studentsMarksView = model.students.map { mapStudentsMarks.fromInternal(it) },
            page = mapPagination.fromInternal(model.page)
        )
    }
}