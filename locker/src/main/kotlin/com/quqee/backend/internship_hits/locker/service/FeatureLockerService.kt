package com.quqee.backend.internship_hits.locker.service

import com.quqee.backend.internship_hits.locker.entity.FeatureLockerEntity
import com.quqee.backend.internship_hits.locker.mapper.FeatureLockerMapper
import com.quqee.backend.internship_hits.locker.repository.FeatureLockerRepository
import com.quqee.backend.internship_hits.public_interface.common.FeatureLockStatusDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.enums.FeatureToLockEnum
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import org.springframework.stereotype.Service

interface FeatureLockerService {

    fun getFeatureLockStatus(featureToLockEnum: FeatureToLockEnum): FeatureLockStatusDto

    fun switchFeatureLockStatus(featureToLockEnum: FeatureToLockEnum): FeatureLockStatusDto

}

@Service
class FeatureLockerServiceImpl(
    private val featureLockerRepository: FeatureLockerRepository,
    private val mapper: FeatureLockerMapper,
) : FeatureLockerService {

    override fun getFeatureLockStatus(featureToLockEnum: FeatureToLockEnum): FeatureLockStatusDto {
        val entity = findFeatureLockStatus(featureToLockEnum)

        return mapper.toDto(entity)
    }

    override fun switchFeatureLockStatus(featureToLockEnum: FeatureToLockEnum): FeatureLockStatusDto {
        val entity = findFeatureLockStatus(featureToLockEnum)

        val newEntity = entity.copy(isLocked = !entity.isLocked)

        featureLockerRepository.save(newEntity)

        return mapper.toDto(newEntity)
    }

    private fun findFeatureLockStatus(featureToLockEnum: FeatureToLockEnum): FeatureLockerEntity {
        return featureLockerRepository.findByFeatureName(featureToLockEnum).orElseThrow {
            ExceptionInApplication(
                ExceptionType.NOT_FOUND,
                "Не найдена фича с именем " + featureToLockEnum.name
            )
        }
    }

}