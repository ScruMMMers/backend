package com.quqee.backend.internship_hits.websocket_common

import java.util.UUID

data class SessionKey(
    val userId: UUID,
    val type: WebSocketHandlerType,
)
