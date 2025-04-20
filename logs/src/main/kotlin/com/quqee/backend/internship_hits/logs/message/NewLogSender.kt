package com.quqee.backend.internship_hits.logs.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.quqee.backend.internship_hits.logs.message.properties.LogsProperties
import com.quqee.backend.internship_hits.public_interface.message.logs.NewLogDto
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class NewLogSender(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val logsProperties: LogsProperties,
    private val objectMapper: ObjectMapper
) : KafkaSender<NewLogDto> {

    private val logger = LoggerFactory.getLogger(NewLogSender::class.java)

    override fun send(message: NewLogDto) {
        val json = objectMapper.writeValueAsString(message)
        logger.info("Отправлена инфа о новом логе `$logsProperties.new.sourceTopic`: $json")
        kafkaTemplate.send(logsProperties.new.sourceTopic, json)
    }
}