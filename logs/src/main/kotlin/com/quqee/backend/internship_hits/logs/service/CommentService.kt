package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.logs.entity.CommentEntity
import com.quqee.backend.internship_hits.logs.mapper.CommentMapper
import com.quqee.backend.internship_hits.logs.repository.jpa.CommentJpaRepository
import com.quqee.backend.internship_hits.logs.specification.CommentSpecification
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.public_interface.comment.CommentsListDto
import com.quqee.backend.internship_hits.public_interface.comment.CreateCommentDto
import com.quqee.backend.internship_hits.public_interface.comment.UpdateCommentDto
import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.NoSuchElementException
import java.util.UUID

interface CommentService {
    fun getCommentsList(logId: UUID, isDeleted: Boolean?, lastId: UUID?, size: Int?): CommentsListDto
    fun createComment(logID: UUID, createCommentDto: CreateCommentDto): CommentDto
    fun updateComment(logID: UUID, commentId: UUID, updateCommentDto: UpdateCommentDto): CommentDto
    fun deleteComment(logID: UUID, commentId: UUID): CommentDto
}

@Service
class CommentServiceImpl(
    private val commentJpaRepository: CommentJpaRepository,
    private val commentMapper: CommentMapper
) : CommentService {
    override fun getCommentsList(logId: UUID, isDeleted: Boolean?, lastId: UUID?, size: Int?): CommentsListDto {
        val pageSize = size ?: DEFAULT_PAGE_SIZE
        val pageable = PageRequest.of(0, pageSize, Sort.by("createdAt").descending())

        var lastComment: CommentEntity? = null
        if (lastId != null) {
            lastComment = commentJpaRepository.findById(lastId).orElse(null)
                ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Комментарий с ID $lastId не найден")
        }

        val spec = Specification.where(CommentSpecification.isDeletedEquals(isDeleted ?: false))
            .and(
                CommentSpecification.createdBefore(
                    lastComment?.createdAt
                )
            )

        val comments = commentJpaRepository.findAll(spec, pageable).content.map { commentMapper.toCommentDto(it) }
        val hasNext = comments.size >= pageSize

        return CommentsListDto(
            comments = comments,
            page = LastIdPagination(
                lastId = if (comments.isNotEmpty()) comments.last().id else null,
                pageSize = pageSize,
                hasNext = hasNext
            )
        )
    }

    override fun createComment(logID: UUID, createCommentDto: CreateCommentDto): CommentDto {
        val myId = getCurrentUser()

        checkMessageEmpty(createCommentDto.message)

        return commentMapper.toCommentDto(
            commentJpaRepository.save(
                commentMapper.toCommentEntity(createCommentDto, myId, logID)
            )
        )
    }

    override fun updateComment(logID: UUID, commentId: UUID, updateCommentDto: UpdateCommentDto): CommentDto {
        val comment = commentJpaRepository.findById(commentId).orElseThrow {
            ExceptionInApplication(
                ExceptionType.NOT_FOUND,
                "Комментарий с идентификатором $commentId не найден"
            )
        }

        checkAccess(comment.author)

        checkMessageEmpty(updateCommentDto.message)

        comment.message = updateCommentDto.message
        comment.updatedAt = OffsetDateTime.now()

        return commentMapper.toCommentDto(
            commentJpaRepository.save(
                comment
            )
        )
    }

    override fun deleteComment(logID: UUID, commentId: UUID): CommentDto {
        val comment = commentJpaRepository.findById(commentId).orElseThrow {
            ExceptionInApplication(
                ExceptionType.NOT_FOUND,
                "Комментарий с идентификатором $commentId не найден"
            )
        }

        checkAccess(comment.author)

        comment.isDeleted = true

        return commentMapper.toCommentDto(
            commentJpaRepository.save(
                comment
            )
        )
    }

    private fun getCurrentUser(): UUID {
        return KeycloakUtils.getUserId() ?: throw ExceptionInApplication(ExceptionType.FORBIDDEN)
    }

    private fun checkAccess(author: UUID) {
        val myId = getCurrentUser()

        if (myId != author) {
            throw ExceptionInApplication(ExceptionType.FORBIDDEN)
        }
    }

    private fun checkMessageEmpty(message: String) {
        if (message.isEmpty()) throw ExceptionInApplication(
            ExceptionType.BAD_REQUEST,
            "Комментарий не может быть пустым"
        )
    }

}