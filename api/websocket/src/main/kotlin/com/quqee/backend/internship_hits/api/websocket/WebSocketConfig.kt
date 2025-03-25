package com.quqee.backend.internship_hits.api.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.converter.DefaultContentTypeResolver
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.converter.MessageConverter
import org.springframework.util.MimeTypeUtils
import org.springframework.web.socket.config.annotation.*

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
open class WebSocketConfig(
    private val notificationHandler: NotificationHandler
) : WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(notificationHandler, "/ws/notification")
            .setAllowedOrigins("*")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS()

        registry.setErrorHandler(WebSocketExceptionInterceptor())
    }

    override fun configureMessageConverters(messageConverters: MutableList<MessageConverter>): Boolean {
        val resolver = DefaultContentTypeResolver()
        resolver.defaultMimeType = MimeTypeUtils.APPLICATION_JSON
        val converter = MappingJackson2MessageConverter()
        val objectMapper = ObjectMapper()
        converter.objectMapper = objectMapper
        converter.contentTypeResolver = resolver
        messageConverters.add(converter)
        return false
    }
}