package com.quqee.backend.internship_hits.logs.specification

import com.quqee.backend.internship_hits.logs.entity.LogEntity
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import org.springframework.data.jpa.domain.Specification
import java.time.OffsetDateTime
import java.util.*

object LogSpecification {

    fun byUserId(userId: UUID?): Specification<LogEntity>? {
        return userId?.let {
            Specification { root, _, cb -> cb.equal(root.get<UUID>("userId"), it) }
        }
    }

    fun byCreatedAtBefore(dateTime: OffsetDateTime?): Specification<LogEntity>? {
        return dateTime?.let {
            Specification { root, _, cb -> cb.lessThan(root.get("createdAt"), it) }
        }
    }

    fun byLogTypes(logTypes: List<LogType>?): Specification<LogEntity>? {
        return if (logTypes.isNullOrEmpty()) null
        else Specification { root, _, _ -> root.get<LogType>("type").`in`(logTypes) }
    }

    fun byApprovalStatuses(statuses: List<ApprovalStatus>?): Specification<LogEntity>? {
        return if (statuses.isNullOrEmpty()) null
        else Specification { root, _, _ -> root.get<ApprovalStatus>("approvalStatus").`in`(statuses) }
    }

    fun byCreatedAtBetween(start: OffsetDateTime, end: OffsetDateTime): Specification<LogEntity>? {
        return Specification { root, _, cb ->
            cb.between(root.get("createdAt"), start, end)
        }
    }
}
