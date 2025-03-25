package com.quqee.backend.internship_hits.api.mapper.comment

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.CommentView
import com.quqee.backend.internship_hits.model.rest.CommentsListView
import com.quqee.backend.internship_hits.model.rest.LastIdPaginationView
import com.quqee.backend.internship_hits.public_interface.comment.CommentsListDto
import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CommentsListConfigurationMapper(
    private val mapComment: FromInternalToApiMapper<CommentView, CommentDto>,
    private val mapPage: FromInternalToApiMapper<LastIdPaginationView, LastIdPagination>
) {
    @Bean
    fun mapCommentsList(): FromInternalToApiMapper<CommentsListView, CommentsListDto> = makeToApiMapper { model ->
        CommentsListView(
            comments = model.comments.map { mapComment.fromInternal(it) },
            page = mapPage.fromInternal(model.page)
        )
    }
}
