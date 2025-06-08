package com.quqee.backend.internship_hits.notification.service.channel

import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.GetProfileDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import com.quqee.backend.internship_hits.public_interface.notification_public.SendNotificationDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class EmailChannelHandler(
    private val emailSender: JavaMailSender,
    private val profileService: ProfileService,
    @Value("\${client.uri}")
    private val clientUri: String,
) : ChannelHandler(
    NotificationChannel.EMAIL
) {

    override fun handle(notification: SendNotificationDto) {
        val profile = profileService.getShortAccount(GetProfileDto(notification.userId))
        val text = formatEmailContent(
            text = notification.message,
            subject = EMAIL_SUBJECT,
            notificationType = notification.type,
        )
        sendEmail(text, profile.email, EMAIL_SUBJECT)
    }

    private fun sendEmail(text: String, to: String, subject: String) {
        try {
            val mimeMessage = emailSender.createMimeMessage()
            val messageHelper = MimeMessageHelper(mimeMessage, true)
            messageHelper.setTo(to)
            messageHelper.setSubject(subject)
            messageHelper.setText(text, true)
            emailSender.send(mimeMessage)
        } catch (e: Exception) {
            throw ExceptionInApplication(ExceptionType.FATAL, "Error while sending email")
        }
    }

    private fun formatEmailContent(
        text: String,
        subject: String,
        notificationType: NotificationType,
    ): String {
        val notificationPageLink = UriComponentsBuilder.fromUriString(clientUri)
            .pathSegment("notifications")
            .queryParam("tab", when (notificationType) {
                NotificationType.SYSTEM -> "system"
                NotificationType.DEANERY -> "announcement"
            })
            .build()
            .toUriString()

        return """
            <!DOCTYPE html>
            <html lang="ru">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width,initial-scale=1"/>
                <title>Уведомление о стажировке</title>
                <style>
                    body {
                        margin:0; padding:0;
                        background: linear-gradient(135deg, #e7f9f4 0%, #eafaf6 100%);
                        font-family: 'Segoe UI', Arial, sans-serif;
                    }
                    .container {
                        max-width:420px;
                        margin:48px auto;
                        background:#fff;
                        border-radius:16px;
                        box-shadow:0 4px 32px rgba(16,185,129,0.10),0 1.5px 4px rgba(16,185,129,0.08);
                        padding:0 0 36px 0;
                        overflow: hidden;
                    }
                    .header {
                        background:#10B981;
                        padding:36px 0 12px 0;
                        text-align:center;
                        position: relative;
                    }
                    .header .icon {
                        background: #fff;
                        color: #10B981;
                        border-radius:50%;
                        display:inline-flex;
                        align-items:center;
                        justify-content:center;
                        width:62px; height:62px;
                        font-size:32px;
                        box-shadow:0 2px 8px rgba(16,185,129, 0.11);
                        margin-bottom:12px;
                    }
                    .header-title {
                        color:#fff;
                        font-size:24px;
                        font-weight:700;
                        letter-spacing:.5px;
                        margin-bottom:4px;
                    }
                    .content {
                        padding:28px 30px 10px 30px;
                        font-size:16px;
                        color:#232323;
                        text-align:center;
                    }
                    .button {
                        display:inline-block;
                        padding:14px 34px;
                        margin-top:24px;
                        background:#10B981; color:#fff;
                        text-decoration:none;
                        border-radius:8px;
                        font-weight:600;
                        font-size:16px;
                        letter-spacing:.5px;
                        box-shadow: 0 2px 10px rgba(16,185,129,0.10);
                        transition: background 0.2s, box-shadow 0.2s;
                    }
                    .button:hover {
                        background: #0ea872;
                        box-shadow: 0 6px 24px rgba(16,185,129,0.18);
                    }
                    .footer {
                        margin-top:30px;
                        font-size:13px;
                        color:#a8a8a8;
                        text-align:center;
                    }
                    .footer a {
                        color:#10B981;
                        text-decoration:underline;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="icon">🎓</div>
                        <div class="header-title">$subject</div>
                    </div>
                    <div class="content">
                        $text
                        <a class="button" href="$notificationPageLink">Посмотреть уведомления</a>
                    </div>
                    <div class="footer">
                        &copy; Internship System, 2025<br>
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()
    }

    companion object {
        private const val EMAIL_SUBJECT = "Вам пришло уведомление"
    }
}