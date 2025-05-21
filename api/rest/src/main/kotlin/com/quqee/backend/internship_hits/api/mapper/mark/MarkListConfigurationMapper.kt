package com.quqee.backend.internship_hits.api.mapper.mark

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.MarkListView
import com.quqee.backend.internship_hits.model.rest.MarkView
import com.quqee.backend.internship_hits.public_interface.mark.MarkDto
import com.quqee.backend.internship_hits.public_interface.mark.MarkListDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class MarkListConfigurationMapper(
    private val mapMark: FromInternalToApiMapper<MarkView, MarkDto>
) {
    @Bean
    open fun mapMarkListToApi(): FromInternalToApiMapper<MarkListView, MarkListDto> = makeToApiMapper { model ->
        MarkListView(
            marks = model.marks.map { mapMark.fromInternal(it) }
        )
    }
}