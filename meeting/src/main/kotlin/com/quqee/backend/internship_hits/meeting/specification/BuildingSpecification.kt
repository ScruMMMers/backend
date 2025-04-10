package com.quqee.backend.internship_hits.meeting.specification

import com.quqee.backend.internship_hits.meeting.entity.BuildingEntity
import com.quqee.backend.internship_hits.meeting.entity.MeetingEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import java.time.OffsetDateTime
import java.util.*

object BuildingSpecification {

    fun byName(name: String?): Specification<BuildingEntity>? {
        return if (name != null) {
            Specification { root, _, cb ->
                cb.like(
                    cb.lower(root.get("name")),
                    "%${name.lowercase()}%"
                )
            }
        } else {
            null
        }
    }

}