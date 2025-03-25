package com.quqee.backend.internship_hits.public_interface.comment

import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination

data class CommentsListDto(
    val comments: List<CommentDto>,
    val page: LastIdPagination
)