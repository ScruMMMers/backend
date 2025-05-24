package com.quqee.backend.internship_hits.profile.client

import com.quqee.backend.internship_hits.profile.dto.CreateUserDto
import com.quqee.backend.internship_hits.profile.dto.UpdateUserDto
import com.quqee.backend.internship_hits.profile.entity.UserEntity
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import org.keycloak.admin.client.CreatedResponseUtil
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.admin.client.resource.UsersResource
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class UserClient(
    @Value("\${keycloak.internship.realm}") private val realm: String,
    private val keycloak: Keycloak,
) {
    fun registerUser(dto: CreateUserDto): UUID {
        val passwordCred = CredentialRepresentation()
        passwordCred.isTemporary = false
        passwordCred.type = CredentialRepresentation.PASSWORD
        passwordCred.value = dto.password

        val userRepresentation = UserRepresentation()
        userRepresentation.username = dto.username
        userRepresentation.email = dto.email
        userRepresentation.firstName = dto.firstName
        userRepresentation.lastName = dto.lastName
        userRepresentation.isEmailVerified = false
        userRepresentation.isEnabled = true
        userRepresentation.credentials = listOf(passwordCred)
        userRepresentation.attributes = mapOf(
            "middleName" to listOf(dto.middleName),
            "photoId" to listOf(dto.photoId),
            "fullName" to listOf(
                getFullName(
                    firstName = dto.firstName,
                    lastName = dto.lastName,
                    middleName = dto.middleName
                )
            ),
        )

        val usersResource = getUsersResource()

        usersResource.create(userRepresentation).use { response ->
            if (201 != response.status) {
                log.error("Error creating user: {}", response.entity)
                throw ExceptionInApplication(ExceptionType.FATAL)
            }
            return UUID.fromString(CreatedResponseUtil.getCreatedId(response))
        }
    }

    fun getUserByUsername(username: String): UserEntity? {
        val usersResource = getUsersResource()
        val userRepresentations = usersResource.searchByUsername(username, true)
        if (userRepresentations.isEmpty()) {
            return null
        }
        val userRepresentation: UserRepresentation? = userRepresentations.firstOrNull()
        return userRepresentation?.let { userRepresentationToEntity(it) }
    }

    fun getUserByEmail(email: String): UserEntity? {
        val usersResource = getUsersResource()
        val userRepresentations = usersResource.searchByEmail(email, true)
        if (userRepresentations.isEmpty()) {
            return null
        }
        val userRepresentation = userRepresentations.firstOrNull()
        return userRepresentation?.let { userRepresentationToEntity(it) }
    }

    fun updateUser(dto: UpdateUserDto) {
        val usersResource = getUsersResource()
        val userRepresentation = usersResource.get(dto.userId.toString()).toRepresentation()

        userRepresentation.username = dto.username
        userRepresentation.email = dto.email
        userRepresentation.firstName = dto.firstName
        userRepresentation.lastName = dto.lastName
        userRepresentation.isEnabled = true
        userRepresentation.attributes = mapOf(
            "middleName" to listOf(dto.middleName),
            "photoId" to listOf(dto.photoId),
            "fullName" to listOf(
                getFullName(
                    firstName = dto.firstName,
                    lastName = dto.lastName,
                    middleName = dto.middleName
                )
            ),
        )

        usersResource.get(dto.userId.toString()).update(userRepresentation)
    }

    fun deleteUser(userId: UUID) {
        val usersResource = getUsersResource()
        usersResource.delete(userId.toString()).use { response ->
            if (204 != response.status) {
                log.error("Error deleting user: {}", response.entity)
                throw ExceptionInApplication(ExceptionType.FATAL)
            }
        }
    }

    fun getUserIdsByName(name: String): Set<UUID> {
        val usersResource = getUsersResource()
        val userRepresentations = usersResource.list()
            .filter {
                it.attributes
                    ?.get("fullName")
                    ?.firstOrNull()
                    ?.contains(normalizeFullName(name)) == true
            }
        return userRepresentations.mapNotNull { it.id?.let(UUID::fromString) }.toSet()
    }

    fun getUser(userId: UUID): UserEntity? {
        val usersResource = getUsersResource()
        val userRepresentation = usersResource[userId.toString()]
            ?: return null
        return userRepresentationToEntity(userRepresentation.toRepresentation())
    }

    private fun getUsersResource(): UsersResource {
        val internshipRealm = keycloak.realm(realm)
        return internshipRealm.users()
    }

    private fun userRepresentationToEntity(userRepresentation: UserRepresentation): UserEntity {
        return UserEntity(
            userId = UUID.fromString(userRepresentation.id),
            username = userRepresentation.username,
            email = userRepresentation.email,
            firstName = userRepresentation.firstName,
            lastName = userRepresentation.lastName,
            roles = getUsersResource()[userRepresentation.id]
                .roles()
                .realmLevel()
                .listAll()
                .filter { it.name.startsWith("ROLE_") }
                .map { UserRole.fromKeycloakRole(it.name) }
                .toSet(),
            middleName = userRepresentation.attributes?.get("middleName")?.firstOrNull(),
            photoId = userRepresentation.attributes?.get("photoId")?.firstOrNull(),
        )
    }

    private fun getFullName(firstName: String, lastName: String, middleName: String?): String {
        return normalizeFullName(formatFullName(firstName, lastName, middleName))
    }

    private fun formatFullName(firstName: String, lastName: String, middleName: String?): String {
        return listOfNotNull(firstName, lastName, middleName).joinToString(" ").trim()
    }

    private fun normalizeFullName(fullName: String): String {
        return fullName
            .replace(Regex("\\s+"), " ")
            .trim()
            .lowercase()
    }

    companion object {
        private val log = LoggerFactory.getLogger(UserClient::class.java)
    }
}