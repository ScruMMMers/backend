package com.quqee.backend.internship_hits.company.specification

import com.quqee.backend.internship_hits.company.entity.CompanyEntity
import org.springframework.data.jpa.domain.Specification
import java.time.OffsetDateTime

object CompanySpecification {

    fun hasNameLike(name: String?): Specification<CompanyEntity>? {
        return if (!name.isNullOrBlank()) {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%${name.lowercase()}%")
            }
        } else {
            null
        }
    }

    fun createdBefore(createdAt: OffsetDateTime?): Specification<CompanyEntity>? {
        return if (createdAt != null) {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.lessThan(root.get("createdAt"), createdAt)
            }
        } else {
            null
        }
    }

    fun isDeleted(isDeleted: Boolean?): Specification<CompanyEntity>? {
        return if (isDeleted != null) {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.equal(root.get<Boolean>("isDeleted"), isDeleted)
            }
        } else {
            null
        }
    }
}