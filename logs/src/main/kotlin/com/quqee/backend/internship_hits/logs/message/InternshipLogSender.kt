package com.quqee.backend.internship_hits.logs.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.quqee.backend.internship_hits.logs.message.properties.LogsProperties
import com.quqee.backend.internship_hits.public_interface.message.logs.NewInternshipDto
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class InternshipLogSender(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val logsProperties: LogsProperties,
    private val objectMapper: ObjectMapper
) : KafkaSender<NewInternshipDto> {

    override fun send(message: NewInternshipDto) {
        val json = objectMapper.writeValueAsString(message)
        kafkaTemplate.send(logsProperties.internship.sourceTopic, json)
    }
}