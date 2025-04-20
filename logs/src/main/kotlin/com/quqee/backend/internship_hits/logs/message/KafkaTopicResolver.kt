package com.quqee.backend.internship_hits.logs.message

import com.quqee.backend.internship_hits.logs.message.config.LogsProperties
import com.quqee.backend.internship_hits.public_interface.message.logs.NewLogDto
import org.springframework.stereotype.Component

@Component
class KafkaTopicResolver(
    private val logsProperties: LogsProperties
) {
    fun <T : Any> resolveTopic(message: T): String {
        return when (message) {
            is NewLogDto -> logsProperties.new.sourceTopic
            else -> logsProperties.internship.sourceTopic
        }
    }
}
