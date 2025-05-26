package com.quqee.backend.internship_hits.profile

import com.quqee.backend.internship_hits.file.service.FileService
import com.quqee.backend.internship_hits.profile.client.RoleClient
import com.quqee.backend.internship_hits.profile.client.UserClient
import com.quqee.backend.internship_hits.profile.dto.CreateUserDto
import com.quqee.backend.internship_hits.profile.dto.UpdateUserDto
import com.quqee.backend.internship_hits.profile.entity.UserEntity
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.common.GetProfileDto
import com.quqee.backend.internship_hits.public_interface.profile_public.ProfileDto
import com.quqee.backend.internship_hits.public_interface.profile_public.ProfileForHeader
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProfileService(
    private val userClient: UserClient,
    private val roleClient: RoleClient,
    private val fileService: FileService,
) {
    fun createProfile(dto: CreateUserDto): UUID {
        if (userClient.getUserByEmail(dto.email) != null) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Пользователь с таким email уже существует")
        }
        if (userClient.getUserByUsername(dto.username) != null) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Пользователь с таким логином уже существует")
        }

        val userId = userClient.registerUser(dto)
        dto.roles.forEach { role ->
            roleClient.assignRole(userId, setOf(role))
        }
        return userId
    }

    fun updateProfile(dto: UpdateUserDto) {
        val user = userClient.getUser(dto.userId) ?:
            throw ExceptionInApplication(ExceptionType.NOT_FOUND)

        if (userClient.getUserByEmail(dto.email) != null) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Пользователь с таким email уже существует")
        }
        if (userClient.getUserByUsername(dto.username) != null) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Пользователь с таким логином уже существует")
        }

        val currentRoles = user.roles
        val newRoles = dto.roles.filter { it !in currentRoles }
        val removedRoles = currentRoles.filter { it !in dto.roles }

        userClient.updateUser(dto)

        if (newRoles.isNotEmpty()) {
            roleClient.assignRole(dto.userId, newRoles.toSet())
        }
        if (removedRoles.isNotEmpty()) {
            roleClient.removeRole(dto.userId, removedRoles.toSet())
        }
    }

    fun getShortAccount(dto: GetProfileDto): ShortAccountDto {
        val profile = getProfile(dto)

        return ShortAccountDto(
            userId = profile.userId,
            fullName = profile.fullName,
            roles = profile.roles.toList(),
            avatarUrl = profile.avatarUrl ?: FALLBACK_PROFILE_IMAGE_URL,
            primaryColor = profile.primaryColor,
            email = profile.email,
        )
    }

    fun getProfileForHeader(dto: GetProfileDto): ProfileForHeader {
        val profile = getProfile(dto)
        return ProfileForHeader(
            fullName = profile.fullName,
            avatarUrl = profile.avatarUrl ?: FALLBACK_PROFILE_IMAGE_URL
        )
    }

    fun getUserRoles(dto: GetProfileDto): Set<UserRole> {
        val user = userClient.getUser(dto.userId) ?:
            throw ExceptionInApplication(ExceptionType.NOT_FOUND)
        return user.roles
    }

    fun getUserIdsByName(name: String): Set<UserId> {
        return userClient.getUserIdsByName(name)
    }

    fun addRoles(userIds: Set<UserId>, roles: Set<UserRole>) {
        runBlocking {
            val deferred = userIds.map { id ->
                async {
                    roleClient.assignRole(id, roles)
                }
            }
            deferred.awaitAll()
        }
    }

    fun removeRoles(userIds: Set<UserId>, roles: Set<UserRole>) {
        runBlocking {
            val deferred = userIds.map { id ->
                async {
                    roleClient.removeRole(id, roles)
                }
            }
            deferred.awaitAll()
        }
    }

    fun getUser(userId: UserId): UserEntity {
        return userClient.getUser(userId) ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND)
    }

    private fun getProfile(dto: GetProfileDto): ProfileDto {
        val user = userClient.getUser(dto.userId) ?:
            throw ExceptionInApplication(ExceptionType.NOT_FOUND)
        val userAvatarUrl = user.photoId?.let { fileService.getFileLink(UUID.fromString(it)) }
        return ProfileDto(
            userId = user.userId,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            roles = user.roles,
            username = user.username,
            avatarUrl = userAvatarUrl?.downloadUrl ?: FALLBACK_PROFILE_IMAGE_URL
        )
    }

    companion object {
        private const val FALLBACK_PROFILE_IMAGE_URL = "https://static.vecteezy.com/system/resources/thumbnails/036/280/651/small_2x/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-illustration-vector.jpg"
        private val log = LoggerFactory.getLogger(ProfileService::class.java)
    }
}