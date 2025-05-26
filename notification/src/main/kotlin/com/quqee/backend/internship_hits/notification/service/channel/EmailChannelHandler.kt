package com.quqee.backend.internship_hits.notification.service.channel

import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.SendNotificationDto
import com.quqee.backend.internship_hits.public_interface.common.GetProfileDto
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class EmailChannelHandler(
    private val emailSender: JavaMailSender,
    private val profileService: ProfileService,
) : ChannelHandler(
    NotificationChannel.EMAIL
) {

    override fun handle(notification: SendNotificationDto) {
        val profile = profileService.getShortAccount(GetProfileDto(notification.userId))
        val text = "${notification.title}\n${notification.message}"
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

    companion object {
        private const val EMAIL_SUBJECT = "Вам пришло уведомление"
    }
}