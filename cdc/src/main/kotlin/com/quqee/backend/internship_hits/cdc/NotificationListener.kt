package com.quqee.backend.internship_hits.cdc

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.quqee.backend.internship_hits.notification.service.NotificationService
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import com.quqee.backend.internship_hits.public_interface.notification_public.SendNotificationDto
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.util.*

@Component
class NotificationListener(
    private val notificationService: NotificationService,
) {
    @KafkaListener(
        topics = ["\${exporter.cdc.notification.source-topic}"],
        groupId = "\${exporter.cdc.notification.group-id}",
        batch = "true"
    )
    fun consume(records: List<ConsumerRecord<String, String>>, ack: Acknowledgment) {
        log.info("Received ${records.size} records")
        try {
            val events = records.mapNotNull(::getEvent)
            notificationService.sendNotifications(events)
        } catch (e: Exception) {
            log.error("Error processing records", e)
        } finally {
            ack.acknowledge()
        }
    }

    private fun getEvent(record: ConsumerRecord<String, String>): SendNotificationDto? {
        return try {
            val payload = jsonMapper.readTree(record.value()).get("payload")
            val operation = payload.get("op").asText()
            if (operation != "c") {
                return null
            }
            val after = payload.get("after")
            convertToEvent(after)
        } catch (e: Exception) {
            log.error("Error parsing record", e)
            null
        }
    }

    private fun convertToEvent(node: JsonNode): SendNotificationDto {
        return SendNotificationDto(
            userId = UUID.fromString(node.get("receiver_id").asText()),
            title = node.get("title").asText(),
            message = node.get("message").asText(),
            channels = node.get("channels").elements()
                .asSequence()
                .map { NotificationChannel.fromDataBase(it.asInt()) }
                .toSet(),
            type = NotificationType.fromDataBase(node.get("type").asInt()),
            id = UUID.fromString(node.get("id").asText())
        )
    }


    companion object {
        private val log = LoggerFactory.getLogger(NotificationListener::class.java)
        private val jsonMapper = jacksonObjectMapper()
    }
}