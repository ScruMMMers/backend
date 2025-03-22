package com.quqee.backend.internship_hits.public_interface.common.exception

enum class UserRole(val keycloakRole: String) {
    BLOCKED("ROLE_BLOCK"),
    DEANERY("ROLE_DEANERY"),
    STUDENT("ROLE_STUDENT");

    companion object {
        fun fromKeycloakRole(keycloakRole: String): UserRole {
            return entries.first { it.keycloakRole == keycloakRole }
        }
    }
}