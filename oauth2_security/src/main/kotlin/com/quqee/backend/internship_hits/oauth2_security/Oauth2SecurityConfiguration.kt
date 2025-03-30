package com.quqee.backend.internship_hits.oauth2_security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.web.cors.CorsConfiguration
import java.util.function.Supplier
import java.util.stream.Stream

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
open class Oauth2SecurityConfiguration {
    @Bean
    @Throws(Exception::class)
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { obj: CorsConfigurer<HttpSecurity> ->
                obj.configurationSource { _ ->
                    val corsConfiguration = CorsConfiguration()
                    corsConfiguration.allowedOrigins = listOf(
                        "https://internship.staziss-tech.ru",
                        "ws://internship.staziss-tech.ru",
                        "https://api.internship.staziss-tech.ru",
                        "ws://api.internship.staziss-tech.ru",
                        "https://localhost:3000",
                        "http://localhost:3000",
                        "ws://localhost:3000",
                        "http://localhost:9000",
                        "ws://localhost:9000",
                    )
                    corsConfiguration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
                    corsConfiguration.allowedHeaders = listOf("*")
                    corsConfiguration
                }
            }
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .sessionManagement { c ->
                c.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .authorizeHttpRequests{ c ->
                c.requestMatchers(*WHITE_LIST).anonymous()
                    .anyRequest().access(customAuthManager())
            }
            .oauth2ResourceServer { oauth2: OAuth2ResourceServerConfigurer<HttpSecurity?> -> oauth2.jwt(Customizer.withDefaults()) }
        return http.build()
    }

    fun customAuthManager(): AuthorizationManager<RequestAuthorizationContext> {
        return AuthorizationManager { authentication: Supplier<Authentication>, _: RequestAuthorizationContext? ->
            authentication.get()
                .authorities
                .stream()
                .filter { authority: GrantedAuthority -> authority.authority == "ROLE_ANONYMOUS" || authority.authority == "ROLE_BLOCK" }
                .findFirst()
                .map { AuthorizationDecision(false) }
                .orElse(AuthorizationDecision(true))
        }
    }

    @Bean
    open fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
        converter.setPrincipalClaimName("preferred_username")
        converter.setJwtGrantedAuthoritiesConverter { jwt: Jwt ->
            val authorities = jwtGrantedAuthoritiesConverter.convert(jwt)
            val roles = jwt.getClaimAsStringList("spring_sec_roles")
            Stream.concat(
                authorities!!.stream(),
                roles.stream()
                    .filter { role: String -> role.startsWith("ROLE_") }
                    .map { role: String? -> SimpleGrantedAuthority(role) }
                    .map { obj: SimpleGrantedAuthority -> GrantedAuthority::class.java.cast(obj) })
                .toList()
        }

        return converter
    }

    companion object {
        private val WHITE_LIST = arrayOf(
            "/hello",
        )
    }
}