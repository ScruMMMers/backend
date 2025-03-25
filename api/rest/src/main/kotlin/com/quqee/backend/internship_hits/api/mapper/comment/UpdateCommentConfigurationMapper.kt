package com.quqee.backend.internship_hits.api.mapper.comment

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.model.rest.UpdateCommentView
import com.quqee.backend.internship_hits.public_interface.comment.UpdateCommentDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UpdateCommentConfigurationMapper {
    @Bean
    fun mapUpdateComment(): FromApiToInternalMapper<UpdateCommentView, UpdateCommentDto> = makeFromApiMapper { model ->
        UpdateCommentDto(
            message = model.message
        )
    }
}
