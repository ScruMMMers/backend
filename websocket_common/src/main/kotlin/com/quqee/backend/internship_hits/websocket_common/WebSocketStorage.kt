package com.quqee.backend.internship_hits.websocket_common

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

@Component
class WebSocketStorage {
    fun add(sessionKey: SessionKey, session: WebSocketSession) {
        SESSIONS[sessionKey] = session
    }

    fun sendMessage(sessionKey: SessionKey, message: Any) {
        val session: WebSocketSession = SESSIONS[sessionKey] ?: return
        if (session.isOpen) {
            try {
                val jsonMessage = mapper.writeValueAsString(message)
                session.sendMessage(TextMessage(jsonMessage))
            } catch (e: IOException) {
                throw ExceptionInApplication(ExceptionType.FATAL, "Exception while sending a message")
            }
        }
    }

    fun remove(session: WebSocketSession) {
        try {
            if (session.isOpen) {
                session.close()
            }
            SESSIONS.entries.removeIf { entry: Map.Entry<SessionKey, WebSocketSession> -> entry.value == session }
        } catch (e: IOException) {
            throw ExceptionInApplication(ExceptionType.FATAL, "Exception while removing a session")
        }
    }

    companion object {
        private val SESSIONS: ConcurrentHashMap<SessionKey, WebSocketSession> = ConcurrentHashMap()
        private val mapper = jacksonObjectMapper()
    }
}