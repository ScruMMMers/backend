package com.quqee.backend.internship_hits.logs.specification

import com.quqee.backend.internship_hits.logs.entity.CommentEntity
import org.springframework.data.jpa.domain.Specification
import java.time.OffsetDateTime
import java.util.UUID

object CommentSpecification {

    fun logIdEquals(logId: UUID): Specification<CommentEntity> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<UUID>("logId"), logId)
        }
    }

    fun replyIdEquals(commentId: UUID): Specification<CommentEntity> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<UUID>("replyTo"), commentId)
        }
    }

    fun createdBefore(createdAt: OffsetDateTime?): Specification<CommentEntity>? {
        return if (createdAt != null) {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.lessThan(root.get("createdAt"), createdAt)
            }
        } else {
            null
        }
    }

    fun replyToIsNull(): Specification<CommentEntity> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.isNull(root.get<UUID>("replyTo"))
        }
    }
}