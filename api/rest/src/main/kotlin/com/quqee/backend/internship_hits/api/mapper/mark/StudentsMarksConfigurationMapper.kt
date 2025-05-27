package com.quqee.backend.internship_hits.api.mapper.mark

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.MarkListView
import com.quqee.backend.internship_hits.model.rest.MarkView
import com.quqee.backend.internship_hits.model.rest.StudentsMarksListView
import com.quqee.backend.internship_hits.model.rest.StudentsMarksView
import com.quqee.backend.internship_hits.public_interface.common.MarkDto
import com.quqee.backend.internship_hits.public_interface.common.MarkListDto
import com.quqee.backend.internship_hits.public_interface.common.StudentsMarksDto
import com.quqee.backend.internship_hits.public_interface.common.StudentsMarksListDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class StudentsMarksConfigurationMapper(
    private val mapMark: FromInternalToApiMapper<MarkView, MarkDto>
) {
    @Bean
    open fun mapStudentsMarksToApi(

    ): FromInternalToApiMapper<StudentsMarksView, StudentsMarksDto> = makeToApiMapper { model ->
        StudentsMarksView(
            id = model.id,
            fullName = model.fullName,
            group = model.group,
            course = model.course,
            marks = model.marks.map { mapMark.fromInternal(it) }
        )
    }
}