package com.quqee.backend.internship_hits.cdc

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.quqee.backend.internship_hits.logs.service.LogsService
import com.quqee.backend.internship_hits.notification.service.NotificationService
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.notification_public.CreateNotificationDto
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import com.quqee.backend.internship_hits.public_interface.profile_public.GetProfileDto
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.util.*

@Component
class LogReactionsListener(
    private val notificationService: NotificationService,
    private val profileService: ProfileService,
    private val logsService: LogsService,
) {
    @KafkaListener(
        topics = ["\${exporter.cdc.log_reactions.source-topic}"],
        groupId = "\${exporter.cdc.log_reactions.group-id}",
        batch = "true"
    )
    fun consume(records: List<ConsumerRecord<String, String>>, ack: Acknowledgment) {
        log.info("Received ${records.size} records")
        try {
            val events = records.mapNotNull(::getEvent)
            runBlocking {
                val deferred = events.map { event ->
                    async {
                        prepareNotification(event)
                    }
                }
                val notifications = deferred.mapNotNull { it.await() }
                notificationService.createNotifications(notifications)
            }
        } catch (e: Exception) {
            log.error("Error processing records", e)
        } finally {
            ack.acknowledge()
        }
    }

    private fun getEvent(record: ConsumerRecord<String, String>): LogReactionBody? {
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

    private fun convertToEvent(node: JsonNode): LogReactionBody {
        return LogReactionBody(
            userId = UUID.fromString(node.get("user_id").asText()),
            id = UUID.fromString(node.get("id").asText()),
            logId = UUID.fromString(node.get("log_id").asText()),
            reactionId = UUID.fromString(node.get("reaction_id").asText()),
        )
    }

    private fun prepareNotification(event: LogReactionBody): CreateNotificationDto? {
        return try {
            runBlocking {
                val profileUserWhoLeaveReactionDeferred = async { profileService.getShortAccount(GetProfileDto(event.userId)) }
                val logDeferred = async { logsService.getLogById(event.logId) }

                val profileUserWhoLeaveReaction = profileUserWhoLeaveReactionDeferred.await()
                val log = logDeferred.await()

                CreateNotificationDto(
                    type = NotificationType.SYSTEM,
                    channels = CHANNELS,
                    title = TITLE,
                    userId = log.author.userId,
                    message = MESSAGE_FORMATTED.format(profileUserWhoLeaveReaction.fullName, log.message),
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val TITLE = "Оставлена реакция под записью"
        private const val MESSAGE_FORMATTED = "Пользователь %s оставил реакцию под вашей записью %s"
        // TODO: пуши пока не сделал поэтому проверяю по почте в будущем нужно будет поставить пуш
        private val CHANNELS = setOf(NotificationChannel.EMAIL)
        private val jsonMapper = jacksonObjectMapper()
        private val log = LoggerFactory.getLogger(LogReactionsListener::class.java)
    }
}

data class LogReactionBody(
    val id: UUID,
    val logId: UUID,
    val reactionId: UUID,
    val userId: UUID,
)