package com.quqee.backend.internship_hits.api.mapper.mark

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.model.rest.CreateMarkView
import com.quqee.backend.internship_hits.model.rest.CreateMarksListView
import com.quqee.backend.internship_hits.public_interface.mark.CreateMarkDto
import com.quqee.backend.internship_hits.public_interface.mark.CreateMarksListDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CreateMarkListConfigurationMapper(
    private val createMarkToDto: FromApiToInternalMapper<CreateMarkView, CreateMarkDto>,
) {
    @Bean
    open fun mapCreateMarkList(): FromApiToInternalMapper<CreateMarksListView, CreateMarksListDto> = makeFromApiMapper { model ->
        CreateMarksListDto(
            marks = model.marks.map { createMarkToDto.fromApi(it) }
        )
    }
}