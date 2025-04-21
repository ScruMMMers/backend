package com.quqee.backend.internship_hits.api.mapper.comment

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.model.rest.CreateCommentView
import com.quqee.backend.internship_hits.public_interface.comment.CreateCommentDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreateCommentConfigurationMapper {
    @Bean
    fun mapCreateComment(): FromApiToInternalMapper<CreateCommentView, CreateCommentDto> = makeFromApiMapper { model ->
        CreateCommentDto(
            message = model.message,
            replyTo = model.replyTo
        )
    }
}
