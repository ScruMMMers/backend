package com.quqee.backend.internship_hits.public_interface.common

import com.quqee.backend.internship_hits.public_interface.common.enums.FeatureToLockEnum

data class FeatureLockStatusDto(
    val featureName: FeatureToLockEnum,
    val isLocked: Boolean
)
