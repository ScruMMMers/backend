package com.quqee.backend.internship_hits.api.mapper.mark

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.MarkView
import com.quqee.backend.internship_hits.public_interface.mark.MarkDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class MarkConfigurationMapper(
) {
    @Bean
    open fun mapMarkFromApi(): FromApiToInternalMapper<MarkView, MarkDto> = makeFromApiMapper { model ->
        MarkDto(
            id = model.id,
            userId = model.userId,
            mark = model.mark,
            date = model.date,
            semester = model.semester,
        )
    }

    @Bean
    open fun mapMarkToApi(): FromInternalToApiMapper<MarkView, MarkDto> = makeToApiMapper { model ->
        MarkView(
            id = model.id,
            userId = model.userId,
            mark = model.mark,
            date = model.date,
            semester = model.semester,
        )
    }
}