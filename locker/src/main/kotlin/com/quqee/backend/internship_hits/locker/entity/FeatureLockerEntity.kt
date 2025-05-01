package com.quqee.backend.internship_hits.locker.entity

import com.quqee.backend.internship_hits.public_interface.common.enums.FeatureToLockEnum
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "feature_locker")
data class FeatureLockerEntity(
    @Id
    var id: UUID,

    @Enumerated(EnumType.STRING)
    @Column(name = "feature_name", nullable = false)
    var featureName: FeatureToLockEnum,

    @Column(name = "is_locked", nullable = false)
    var isLocked: Boolean
) {
    constructor() : this(
        UUID.randomUUID(),
        FeatureToLockEnum.SUBMITTING_PRACTICE_DIARY,
        false
    )
}