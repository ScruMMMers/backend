package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.locker.service.FeatureLockerService
import com.quqee.backend.internship_hits.model.rest.FeatureLockStatusView
import com.quqee.backend.internship_hits.model.rest.FeatureToLockEnum
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class FeatureLockerController(
    private val featureLockerService: FeatureLockerService
) : LockerApiDelegate {

    override fun lockerFeatureNameGetGet(featureName: FeatureToLockEnum): ResponseEntity<FeatureLockStatusView> {
        val dto =
            featureLockerService.getFeatureLockStatus(
                com.quqee.backend.internship_hits.public_interface.common.enums.FeatureToLockEnum.valueOf(
                    featureName.value
                )
            )

        return ResponseEntity.ok(
            FeatureLockStatusView(
                FeatureToLockEnum.valueOf(dto.featureName.toString()),
                dto.isLocked
            )
        )
    }

    override fun lockerFeatureNameSwitchPut(featureName: FeatureToLockEnum): ResponseEntity<FeatureLockStatusView> {
        val dto =
            featureLockerService.getFeatureLockStatus(
                com.quqee.backend.internship_hits.public_interface.common.enums.FeatureToLockEnum.valueOf(
                    featureName.value
                )
            )

        return ResponseEntity.ok(
            FeatureLockStatusView(
                FeatureToLockEnum.valueOf(dto.featureName.toString()),
                dto.isLocked
            )
        )
    }

}