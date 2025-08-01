package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.announcement.AnnouncementService
import com.quqee.backend.internship_hits.logs.entity.CommentEntity
import com.quqee.backend.internship_hits.logs.mapper.CommentMapper
import com.quqee.backend.internship_hits.logs.repository.jpa.CommentJpaRepository
import com.quqee.backend.internship_hits.logs.repository.jpa.LogsJpaRepository
import com.quqee.backend.internship_hits.logs.specification.CommentSpecification
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.public_interface.announcement_interface.AnnouncementDataDto
import com.quqee.backend.internship_hits.public_interface.announcement_interface.CreateAnnouncementDto
import com.quqee.backend.internship_hits.public_interface.comment.CommentsListDto
import com.quqee.backend.internship_hits.public_interface.comment.CreateCommentDto
import com.quqee.backend.internship_hits.public_interface.comment.UpdateCommentDto
import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.common.CommentWithoutRepliesDto
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import java.util.*

interface CommentService {
    fun getCommentsList(logId: UUID, lastId: UUID?, size: Int?): CommentsListDto
    fun getCommentReplies(commentId: UUID): List<CommentWithoutRepliesDto>
    fun createComment(logID: UUID, createCommentDto: CreateCommentDto): CommentDto
    fun updateComment(logID: UUID, commentId: UUID, updateCommentDto: UpdateCommentDto): CommentDto
    fun deleteComment(logID: UUID, commentId: UUID): CommentDto
}

@Service
class CommentServiceImpl(
    private val commentJpaRepository: CommentJpaRepository,
    private val commentMapper: CommentMapper,
    private val logsJpaRepository: LogsJpaRepository,
    private val announcementService: AnnouncementService,
) : CommentService {
    override fun getCommentsList(logId: UUID, lastId: UUID?, size: Int?): CommentsListDto {
        val pageSize = size ?: DEFAULT_PAGE_SIZE
        val pageable = PageRequest.of(0, pageSize, Sort.by("createdAt").ascending())

        var lastComment: CommentEntity? = null
        if (lastId != null) {
            lastComment = commentJpaRepository.findById(lastId).orElse(null)
                ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Комментарий с ID $lastId не найден")
        }

        val spec = Specification
            .where(CommentSpecification.createdBefore(lastComment?.createdAt))
            .and(CommentSpecification.logIdEquals(logId))
            .and(CommentSpecification.replyToIsNull())

        val comments = commentJpaRepository.findAll(spec, pageable).content.map {
            commentMapper.toCommentDto(
                it,
                getCommentReplies(it.id)
            )
        }
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

    override fun getCommentReplies(commentId: UUID): List<CommentWithoutRepliesDto> {
        val spec = Specification.where(CommentSpecification.replyIdEquals(commentId))
        val sort = Sort.by("createdAt").ascending()
        val comments = commentJpaRepository.findAll(spec, sort).map { commentMapper.toCommentWithoutRepliesDto(it) }
        return comments
    }

    override fun createComment(logID: UUID, createCommentDto: CreateCommentDto): CommentDto {
        val myId = getCurrentUser()
        val log = logsJpaRepository.findById(logID).orElseThrow {
            ExceptionInApplication(ExceptionType.BAD_REQUEST, "Лог с ID $logID не найден")
        }

        checkMessageEmpty(createCommentDto.message)

        createCommentDto.replyTo?.let {
            val parentComment = commentJpaRepository.findById(it).orElseThrow {
                ExceptionInApplication(ExceptionType.NOT_FOUND, "Комментарий с идентификатором $it не найден")
            }

            if (parentComment.replyTo != null) {
                throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Нельзя ответить на комментарий ответа")
            }

            if (parentComment.logId != logID) {
                throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Комментарий относится к другому логу")
            }
        }

        val savedComment = commentJpaRepository.save(
            commentMapper.toCommentEntity(createCommentDto, myId, logID)
        )

        // Получение пользователей, которые уже оставили комментарии к этому логу, за исключением себя
        val usersWhoCommented = (commentJpaRepository.findDistinctUsersByLogId(logID) + log.userId).toSet().filter { it != myId }
        announcementService.sendSystemAnnouncementByUserId(CreateAnnouncementDto(
            data = AnnouncementDataDto(
                title = "Новый ответ на лог",
                text = "Пользователь ${KeycloakUtils.getUsername()} ответил: ${savedComment.message}",
                redirectId = log.userId
            ),
            userIds = usersWhoCommented
        ))

        return commentMapper.toCommentDto(
            savedComment,
            getCommentReplies(savedComment.id)
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
            ),
            getCommentReplies(commentId)
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
            ),
            getCommentReplies(commentId)
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