package com.quqee.backend.internship_hits.locker.mapper

import com.quqee.backend.internship_hits.locker.entity.FeatureLockerEntity
import com.quqee.backend.internship_hits.public_interface.common.FeatureLockStatusDto
import org.springframework.stereotype.Component

@Component
class FeatureLockerMapper {

    fun toDto(entity: FeatureLockerEntity): FeatureLockStatusDto {
        return FeatureLockStatusDto(
            entity.featureName,
            entity.isLocked
        )
    }

}