package com.quqee.backend.internship_hits.oauth2_security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.security.Principal
import java.util.*

object KeycloakUtils {
    fun getUserId(principal: Principal): UUID? {
        val id = if (principal is JwtAuthenticationToken) {
             principal.getTokenAttributes().get("sub").toString()
        } else {
            null
        }
        return id?.let { UUID.fromString(id) }
    }

    fun getUserId(): UUID? {
        val principal = getPrincipal() ?: return null
        return try {
            UUID.fromString(principal.claims["sub"] as String)
        } catch (e: Exception) {
            null
        }
    }

    fun getUsername(): String? {
        val principal = getPrincipal() ?: return null
        return principal.claims["preferred_username"] as? String
            ?: principal.claims["name"] as? String
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