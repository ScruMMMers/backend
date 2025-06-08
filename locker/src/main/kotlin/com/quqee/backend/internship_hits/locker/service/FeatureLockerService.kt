package com.quqee.backend.internship_hits.locker.service

import com.quqee.backend.internship_hits.announcement.AnnouncementService
import com.quqee.backend.internship_hits.locker.entity.FeatureLockerEntity
import com.quqee.backend.internship_hits.locker.mapper.FeatureLockerMapper
import com.quqee.backend.internship_hits.locker.notification.FeatureNotificationTemplates
import com.quqee.backend.internship_hits.locker.repository.FeatureLockerRepository
import com.quqee.backend.internship_hits.public_interface.announcement_interface.AnnouncementDataDto
import com.quqee.backend.internship_hits.public_interface.announcement_interface.CreateAnnouncementByFilterDto
import com.quqee.backend.internship_hits.public_interface.announcement_interface.CreateAnnouncementDto
import com.quqee.backend.internship_hits.public_interface.common.FeatureLockStatusDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.enums.FeatureToLockEnum
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.students_public.GetStudentsListFilterParamDto
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

interface FeatureLockerService {

    fun getFeatureLockStatus(): List<FeatureLockStatusDto>

    fun switchFeatureLockStatus(featureToLockEnum: FeatureToLockEnum): FeatureLockStatusDto

}

@Service
class FeatureLockerServiceImpl(
    private val featureLockerRepository: FeatureLockerRepository,
    private val mapper: FeatureLockerMapper,
    private val announcementService: AnnouncementService
) : FeatureLockerService {

    override fun getFeatureLockStatus(): List<FeatureLockStatusDto> {
        val entities = featureLockerRepository.findAll(Sort.by("featureName").descending())

        return entities.map { mapper.toDto(it) }
    }

    override fun switchFeatureLockStatus(featureToLockEnum: FeatureToLockEnum): FeatureLockStatusDto {
        val entity = findFeatureLockStatus(featureToLockEnum)

        val newEntity = entity.copy(isLocked = !entity.isLocked)

        featureLockerRepository.save(newEntity)

        sendFeatureLockAnnouncement(featureToLockEnum, newEntity.isLocked)

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

    private fun sendFeatureLockAnnouncement(featureToLockEnum: FeatureToLockEnum, isLocked: Boolean) {
        val announcementData = when (featureToLockEnum) {
            FeatureToLockEnum.SUBMITTING_PRACTICE_DIARY -> {
                if (isLocked) {
                    AnnouncementDataDto(
                        title = FeatureNotificationTemplates.PRACTICE_DIARY_SUBMISSION_ENDED.title,
                        text = FeatureNotificationTemplates.PRACTICE_DIARY_SUBMISSION_ENDED.message
                    )
                } else {
                    AnnouncementDataDto(
                        title = FeatureNotificationTemplates.PRACTICE_DIARY_SUBMISSION_STARTED.title,
                        text = FeatureNotificationTemplates.PRACTICE_DIARY_SUBMISSION_STARTED.message
                    )
                }
            }
            FeatureToLockEnum.CHANGING_INTERNSHIP_LOCATION -> {
                if (isLocked) {
                    AnnouncementDataDto(
                        title = FeatureNotificationTemplates.COMPANY_CHANGE_NOT_ALLOWED.title,
                        text = FeatureNotificationTemplates.COMPANY_CHANGE_NOT_ALLOWED.message
                    )
                } else {
                    AnnouncementDataDto(
                        title = FeatureNotificationTemplates.COMPANY_CHANGE_ALLOWED.title,
                        text = FeatureNotificationTemplates.COMPANY_CHANGE_ALLOWED.message
                    )
                }
            }
        }

        val filter = GetStudentsListFilterParamDto(
            course = setOf(3, 4)
        )

        val announcementDto = CreateAnnouncementByFilterDto(
            data = announcementData,
            filter = filter,
            excludeUserIds = null
        )

        announcementService.sendAnnouncementByFilter(announcementDto)
    }
}