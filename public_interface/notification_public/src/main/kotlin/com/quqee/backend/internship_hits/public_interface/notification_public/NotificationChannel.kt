package com.quqee.backend.internship_hits.public_interface.notification_public

enum class NotificationChannel(val value: Int) {
    EMAIL(1),
    PUSH(2);

    fun toDataBase(): Int {
        return value
    }

    companion object {
        fun fromDataBase(value: Int): NotificationChannel {
            return NotificationChannel.entries.first { it.value == value }
        }

        fun fromString(value: String): NotificationChannel {
            return NotificationChannel.valueOf(value)
        }
    }
}