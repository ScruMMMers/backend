package com.quqee.backend.internship_hits.public_interface.enums

enum class LogType(
    val statisticName: String
) {
    DEFAULT("Статистика по обычным сообщениям"),
    OFFER("Статистика по полученным офферам"),
    INTERVIEW("Статистика по проведенным интервью"),
    FINAL("Статистика по финальному выбору студентов"),
    PRACTICE_DIARY("Статистика по сданным дневникам практики")
}