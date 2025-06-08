package com.quqee.backend.internship_hits.locker.notification

object FeatureNotificationTemplates {
    val PRACTICE_DIARY_SUBMISSION_STARTED = NotificationTemplate(
        title = "Началась сдача дневников практики",
        message = "Сдача дневников практики началась. Пожалуйста, не забудьте отправить свои дневники."
    )

    val PRACTICE_DIARY_SUBMISSION_ENDED = NotificationTemplate(
        title = "Закончилась сдача дневников практики",
        message = "Сдача дневников практики завершена. Кто не успел, тот опоздал!"
    )

    val COMPANY_CHANGE_ALLOWED = NotificationTemplate(
        title = "Можно сменить компанию",
        message = "Вы можете сменить компанию для прохождения практики. По всем вопросам обращайтесь к деканату."
    )

    val COMPANY_CHANGE_NOT_ALLOWED = NotificationTemplate(
        title = "Больше нельзя менять компанию",
        message = "Смена компании больше недоступна. Пожалуйста, свяжитесь с деканатом, если у вас есть вопросы."
    )
}

data class NotificationTemplate(
    val title: String,
    val message: String
)
