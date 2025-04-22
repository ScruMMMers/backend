package com.quqee.backend.internship_hits.profile.client

import com.quqee.backend.internship_hits.profile.dto.CreateUserDto
import com.quqee.backend.internship_hits.profile.entity.UserEntity
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
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

    fun deleteUser(userId: UUID) {
        val usersResource = getUsersResource()
        usersResource.delete(userId.toString()).use { response ->
            if (204 != response.status) {
                log.error("Error deleting user: {}", response.entity)
                throw ExceptionInApplication(ExceptionType.FATAL)
            }
        }
    }

    fun getUser(userId: UUID): UserEntity? {
        val usersResource = getUsersResource()
        val userRepresentation = usersResource[userId.toString()]
            ?: return null
        return userRepresentationToEntity(userRepresentation)
    }

    private fun getUsersResource(): UsersResource {
        val internshipRealm = keycloak.realm(realm)
        return internshipRealm.users()
    }

    private fun userRepresentationToEntity(resource: UserResource): UserEntity {
        val userRepresentation = resource.toRepresentation()
        return UserEntity(
            userId = UUID.fromString(userRepresentation.id),
            username = userRepresentation.username,
            email = userRepresentation.email,
            firstName = userRepresentation.firstName,
            lastName = userRepresentation.lastName,
            roles = resource.roles()
                .realmLevel()
                .listAll()
                .filter { it.name.startsWith("ROLE_") }
                .map { UserRole.fromKeycloakRole(it.name) }
                .toSet(),
            middleName = userRepresentation.attributes["middleName"]?.toString(),
            photoId = userRepresentation.attributes["photoId"]?.toString(),
        )
    }

    companion object {
        private val log = LoggerFactory.getLogger(UserClient::class.java)
    }
}