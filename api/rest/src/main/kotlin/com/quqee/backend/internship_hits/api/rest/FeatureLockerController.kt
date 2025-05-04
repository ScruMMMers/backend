package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.locker.service.FeatureLockerService
import com.quqee.backend.internship_hits.model.rest.FeatureLockStatusListView
import com.quqee.backend.internship_hits.model.rest.FeatureLockStatusView
import com.quqee.backend.internship_hits.model.rest.FeatureToLockEnum
import com.quqee.backend.internship_hits.public_interface.common.FeatureLockStatusDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class FeatureLockerController(
    private val featureLockerService: FeatureLockerService
) : LockerApiDelegate {

    override fun lockerFeaturesGet(): ResponseEntity<FeatureLockStatusListView> {
        val dtos =
            featureLockerService.getFeatureLockStatus()

        return ResponseEntity.ok(
            FeatureLockStatusListView(dtos.map { mapDtoToView(it) })
        )
    }

    override fun lockerFeatureNameSwitchPut(featureName: FeatureToLockEnum): ResponseEntity<FeatureLockStatusView> {
        val dto =
            featureLockerService.switchFeatureLockStatus(
                com.quqee.backend.internship_hits.public_interface.common.enums.FeatureToLockEnum.valueOf(
                    featureName.value
                )
            )

        return ResponseEntity.ok(
            mapDtoToView(dto)
        )
    }

    private fun mapDtoToView(dto: FeatureLockStatusDto): FeatureLockStatusView {
        return FeatureLockStatusView(
            FeatureToLockEnum.valueOf(dto.featureName.toString()),
            dto.isLocked
        )
    }

}