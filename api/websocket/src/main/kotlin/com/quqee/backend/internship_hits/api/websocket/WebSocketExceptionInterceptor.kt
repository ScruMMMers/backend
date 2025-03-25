package com.quqee.backend.internship_hits.api.websocket

import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import org.springframework.messaging.Message
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler

class WebSocketExceptionInterceptor : StompSubProtocolErrorHandler() {
    override fun handleClientMessageProcessingError(
        clientMessage: Message<ByteArray>?,
        ex: Throwable
    ): Message<ByteArray>? {
        if (ex is ExceptionInApplication && clientMessage != null) {
            return handleExceptionInApplication(clientMessage, ex)
        }

        return super.handleClientMessageProcessingError(clientMessage, ex)
    }

    private fun handleExceptionInApplication(
        clientMessage: Message<ByteArray>,
        exception: ExceptionInApplication
    ): Message<ByteArray> {
        val accessor = StompHeaderAccessor.create(StompCommand.ERROR)
        setReceiptIdForClient(clientMessage, accessor)
        accessor.message = STATUS_CODE_MAP[exception.type]
        accessor.setLeaveMutable(true)

        val message = transformMessageToJSONString(exception.message)

        return MessageBuilder.createMessage(message.toByteArray(), accessor.messageHeaders)
    }

    private fun transformMessageToJSONString(message: String): String {
        return "{\"message\": \"$message\"}"
    }

    private fun setReceiptIdForClient(clientMessage: Message<ByteArray>, accessor: StompHeaderAccessor) {
        val clientAccessor = StompHeaderAccessor.wrap(clientMessage)
        val receiptId = clientAccessor.receipt
        if (receiptId != null) {
            accessor.receiptId = receiptId
        }
    }

    companion object {
        private val STATUS_CODE_MAP: Map<ExceptionType, String> = java.util.Map.of<ExceptionType, String>(
            ExceptionType.FATAL, "1011",
            ExceptionType.BAD_REQUEST, "1008",
            ExceptionType.NOT_FOUND, "1008",
            ExceptionType.FORBIDDEN, "1008"
        )
    }
}