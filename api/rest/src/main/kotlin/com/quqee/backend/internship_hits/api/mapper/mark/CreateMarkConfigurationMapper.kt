package com.quqee.backend.internship_hits.api.mapper.mark

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.model.rest.CreateMarkView
import com.quqee.backend.internship_hits.public_interface.mark.CreateMarkDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CreateMarkConfigurationMapper(
) {
    @Bean
    open fun mapCreateMark(): FromApiToInternalMapper<CreateMarkView, CreateMarkDto> = makeFromApiMapper { model ->
        CreateMarkDto(
            mark = model.mark,
            semester = model.semester
        )
    }
}