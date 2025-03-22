package com.quqee.backend.internship_hits.profile.configuration

import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.quqee.backend.internship_hits.file_storage",
    ]
)
open class ProfileBeanConfiguration {
    @Bean
    open fun keycloak(
        @Value("\${keycloak.internship.uri}") uri: String,
        @Value("\${keycloak.internship.realm}") realm: String,
        @Value("\${keycloak.internship.client-id}") clientId: String,
        @Value("\${keycloak.internship.client-secret}") clientSecret: String,
    ): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(uri)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .realm(realm)
            .build()
    }
}