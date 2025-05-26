package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.mapper.EnumerationMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.marks.service.MarkService
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.notification.service.NotificationService
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.PositionDto
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.common.MarkListDto
import com.quqee.backend.internship_hits.public_interface.notification_public.GetNotificationForHeaderDto
import com.quqee.backend.internship_hits.public_interface.notification_public.ShortNotificationDto
import com.quqee.backend.internship_hits.public_interface.common.GetProfileDto
import com.quqee.backend.internship_hits.students.StudentsService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.net.URI

@Component
class ProfileController(
    private val profileService: ProfileService,
    private val notificationService: NotificationService,
    private val studentsService: StudentsService,
    private val markService: MarkService,
    private val shortNotificationMapper: FromInternalToApiMapper<ShortNotificationView, ShortNotificationDto>,
    private val shortCompanyMapper: FromInternalToApiMapper<ShortCompanyView, ShortCompanyDto>,
    private val userRoleMapper: EnumerationMapper<RoleEnum, UserRole>,
    private val mapMarkToApi: FromInternalToApiMapper<MarkListView, MarkListDto>,
    private val mapPositionToApi: FromInternalToApiMapper<PositionView, PositionDto>,
) : ProfileApiDelegate {
    override fun profileHeaderGet(): ResponseEntity<GetProfileHeaderResponseView> {
        val userId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.FORBIDDEN)
        val headerProfile = profileService.getProfileForHeader(GetProfileDto(userId))
        val notifications = notificationService.getNotificationsForHeader(GetNotificationForHeaderDto(userId))

        return ResponseEntity.ok(
            GetProfileHeaderResponseView(
                fullName = headerProfile.fullName,
                avatarUrl = URI.create(headerProfile.avatarUrl),
                notifications = notifications.map { shortNotificationMapper.fromInternal(it) }
            )
        )
    }

    override fun profileMeGet(): ResponseEntity<GetMyProfileResponseView> {
        val userId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.FORBIDDEN)
        val profile = profileService.getShortAccount(GetProfileDto(userId))
        val student = try {
            studentsService.getStudent(userId)
        } catch (ignore: Exception) {null}
        val mark = try {
            markService.getMyMarks()
        } catch (ignore: Exception) {null}

        return ResponseEntity.ok(
            GetMyProfileResponseView(
                profile = ProfileView(
                    fullName = profile.fullName,
                    avatarUrl = URI.create(profile.avatarUrl),
                    roles = profile.roles.map { userRoleMapper.mapToApi(it) },
                    course = student?.course?.toString(),
                    group = student?.group,
                    shortCompany = student?.company?.let { shortCompanyMapper.fromInternal(it) },
                    mark = mark?.let { mapMarkToApi.fromInternal(it) },
                    position = student?.position?.let { mapPositionToApi.fromInternal(it) }
                )
            )
        )
    }

    override fun profileRolesGet(): ResponseEntity<GetMyRolesView> {
        val userId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.FORBIDDEN)
        val roles = profileService.getUserRoles(GetProfileDto(userId))

        return ResponseEntity.ok(
            GetMyRolesView(
                roles = roles.map { userRoleMapper.mapToApi(it) }
            )
        )
    }
}
