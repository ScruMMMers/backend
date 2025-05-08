package com.quqee.backend.internship_hits.profile.client

import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.RolesResource
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class RoleClient(
    @Value("\${keycloak.internship.realm}") private val realm: String,
    private val keycloak: Keycloak,
) {
    fun assignRole(userId: UUID, roles: Set<UserRole>) {
        val userResource = keycloak.realm(realm).users().get(userId.toString())
        val rolesResource: RolesResource = getRolesResource()
        val representations = rolesResource.list()
            .filter { it.name in roles.map { it.keycloakRole } }
        userResource.roles().realmLevel().add(representations)
    }

    fun removeRole(userId: UUID, roles: Set<UserRole>) {
        val userResource = keycloak.realm(realm).users().get(userId.toString())
        val rolesResource: RolesResource = getRolesResource()
        val representations = rolesResource.list()
            .filter { it.name in roles.map { it.keycloakRole } }
        userResource.roles().realmLevel().remove(representations)
    }

    private fun getRolesResource(): RolesResource {
        return keycloak.realm(realm).roles()
    }
}