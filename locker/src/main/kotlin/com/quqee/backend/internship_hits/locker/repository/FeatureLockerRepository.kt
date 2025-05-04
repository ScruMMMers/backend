package com.quqee.backend.internship_hits.locker.repository

import com.quqee.backend.internship_hits.locker.entity.FeatureLockerEntity
import com.quqee.backend.internship_hits.public_interface.common.enums.FeatureToLockEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FeatureLockerRepository : JpaRepository<FeatureLockerEntity, UUID> {

    fun findByFeatureName(enum: FeatureToLockEnum): Optional<FeatureLockerEntity>

}