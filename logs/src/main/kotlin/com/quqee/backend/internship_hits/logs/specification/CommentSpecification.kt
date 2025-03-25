package com.quqee.backend.internship_hits.logs.specification

import com.quqee.backend.internship_hits.logs.entity.CommentEntity
import org.springframework.data.jpa.domain.Specification
import java.time.OffsetDateTime

object CommentSpecification {

    fun isDeletedEquals(isDeleted: Boolean): Specification<CommentEntity> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<Boolean>("isDeleted"), isDeleted)
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
}