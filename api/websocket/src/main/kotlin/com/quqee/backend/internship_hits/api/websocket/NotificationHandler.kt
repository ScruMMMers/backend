package com.quqee.backend.internship_hits.api.websocket

import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.websocket_common.SessionKey
import com.quqee.backend.internship_hits.websocket_common.WebSocketHandlerType
import com.quqee.backend.internship_hits.websocket_common.WebSocketStorage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.AbstractWebSocketHandler
import java.util.UUID

@Component
class NotificationHandler(
    private val webSocketStorage: WebSocketStorage,
) : AbstractWebSocketHandler() {
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = getUserId(session)
        val sessionKey = SessionKey(userId, WebSocketHandlerType.NOTIFICATIONS)
        webSocketStorage.add(sessionKey, session)
    }

    @Throws(Exception::class)
    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        log.info("Exception occurred: {} on session: {}", exception.message, session.id)
        session.close(CloseStatus.SERVER_ERROR.withReason(exception.message!!))
        webSocketStorage.remove(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        webSocketStorage.remove(session)
    }

    private fun getUserId(session: WebSocketSession): UUID {
        val principal = session.principal
        return principal?.let { KeycloakUtils.getUserId(it) } ?:
            throw ExceptionInApplication(ExceptionType.FATAL, "Invalid principal")
    }

    companion object {
        private val log = LoggerFactory.getLogger(NotificationHandler::class.java)
    }
}