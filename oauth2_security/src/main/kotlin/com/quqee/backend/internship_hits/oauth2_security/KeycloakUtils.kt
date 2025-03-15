package com.quqee.backend.internship_hits.oauth2_security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import java.util.*

object KeycloakUtils {
    fun getUserId(): UUID? {
        val principal = getPrincipal() ?: return null
        return try {
            UUID.fromString(principal.claims["sub"] as String)
        } catch (e: Exception) {
            null
        }
    }

    private fun getPrincipal(): Jwt? {
        return try {
            SecurityContextHolder.getContext()
                .authentication
                .principal as Jwt
        } catch (e: Exception) {
            null
        }
    }
}