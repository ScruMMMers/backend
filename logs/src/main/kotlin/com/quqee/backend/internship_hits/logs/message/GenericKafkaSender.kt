package com.quqee.backend.internship_hits.logs.message

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class GenericKafkaSender<T : Any>(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    private val topicResolver: KafkaTopicResolver
) : KafkaSender<T> {

    private val logger = LoggerFactory.getLogger(GenericKafkaSender::class.java)

    override fun send(message: T) {
        val json = try {
            objectMapper.writeValueAsString(message)
        } catch (e: Exception) {
            logger.error("Ошибка сериализации сообщения: $message", e)
            return
        }

        val topic = try {
            topicResolver.resolveTopic(message)
        } catch (e: Exception) {
            logger.error("Ошибка определения топика для сообщения: $message", e)
            return
        }

        kafkaTemplate.send(topic, json)
            .whenComplete { _, ex ->
                if (ex != null) {
                    logger.error("Ошибка отправки сообщения в `$topic`: $json", ex)
                } else {
                    logger.info("Отправлено сообщение в `$topic`: $json")
                }
            }
    }
}