package com.quqee.backend.internship_hits.profile.client

import com.quqee.backend.internship_hits.public_interface.common.exception.UserRole
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
    fun assignRole(userId: UUID, role: UserRole) {
        val userResource = keycloak.realm(realm).users().get(userId.toString())
        val rolesResource: RolesResource = getRolesResource()
        val representation = rolesResource[role.keycloakRole].toRepresentation()
        userResource.roles().realmLevel().add(listOf(representation))
    }

    fun removeRole(userId: UUID, role: UserRole) {
        val userResource = keycloak.realm(realm).users().get(userId.toString())
        val rolesResource: RolesResource = getRolesResource()
        val representation = rolesResource[role.keycloakRole].toRepresentation()
        userResource.roles().realmLevel().remove(listOf(representation))
    }

    private fun getRolesResource(): RolesResource {
        return keycloak.realm(realm).roles()
    }
}