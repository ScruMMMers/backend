package com.quqee.backend.internship_hits.public_interface.notification_public

enum class NotificationType(val value: Int) {
    SYSTEM(0),
    DEANERY(1);

    fun toDataBase(): Int {
        return value
    }

    companion object {
        fun fromDataBase(value: Int): NotificationType {
            return entries.first { it.value == value }
        }
    }
}