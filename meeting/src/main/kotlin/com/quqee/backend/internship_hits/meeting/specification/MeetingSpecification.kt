package com.quqee.backend.internship_hits.meeting.specification

import com.quqee.backend.internship_hits.meeting.entity.MeetingEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import java.time.OffsetDateTime
import java.util.*

object MeetingSpecification {

    fun byCompanyId(companyId: UUID?): Specification<MeetingEntity>? {
        return if (companyId != null) {
            Specification { root, _, cb ->
                cb.equal(root.get<UUID>("companyId"), companyId)
            }
        } else {
            null
        }
    }

    fun byUpcoming(upcoming: Boolean?): Specification<MeetingEntity>? {
        if (upcoming != null) {
            return Specification { root, _, cb ->
                val now = OffsetDateTime.now()
                if (upcoming) {
                    cb.greaterThan(root.get("date"), now)
                } else {
                    cb.lessThanOrEqualTo(root.get("date"), now)
                }
            }
        }
        return null
    }

    fun byDateAt(dateAt: OffsetDateTime?, upcoming: Boolean?): Specification<MeetingEntity>? {
        return dateAt?.let {
            Specification { root, _, cb ->
                val predicates = mutableListOf<Predicate>()
                if (upcoming == true) {
                    predicates.add(cb.greaterThan(root.get("date"), it))
                } else {
                    predicates.add(cb.lessThan(root.get("date"), it))
                }

                cb.and(*predicates.toTypedArray())
            }
        }
    }

}