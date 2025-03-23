package com.quqee.backend.internship_hits.public_interface.common.enums

enum class UserRole(
    val keycloakRole: String,
    val roleColor: ColorEnum,
    val priority: Int
) {
    BLOCKED("ROLE_BLOCK", ColorEnum.NAVY, Int.MAX_VALUE),
    DEANERY("ROLE_DEANERY", ColorEnum.RED, 100),
    STUDENT_SECOND("ROLE_STUDENT_SECOND", ColorEnum.GREEN, 1);

    companion object {
        fun fromKeycloakRole(keycloakRole: String): UserRole {
            return entries.first { it.keycloakRole == keycloakRole }
        }
    }
}