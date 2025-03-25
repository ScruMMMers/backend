package com.quqee.backend.internship_hits.logs.mapper

import com.quqee.backend.internship_hits.logs.entity.LogEntity
import com.quqee.backend.internship_hits.logs.service.ReactionService
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
import com.quqee.backend.internship_hits.public_interface.logs.LogDto
import com.quqee.backend.internship_hits.public_interface.profile_public.GetProfileDto
import com.quqee.backend.internship_hits.public_interface.common.TagDto
import com.quqee.backend.internship_hits.tags.entity.TagEntity
import org.springframework.stereotype.Component
import java.net.URI
import java.time.OffsetDateTime
import java.util.*

@Component
class LogMapper(
    private val reactionService: ReactionService,
    private val profileService: ProfileService,
) {
    /**
     * Преобразование сущности лога в DTO представление
     */
    fun toLogView(entity: LogEntity): LogDto {
        return LogDto(
            id = entity.id,
            message = entity.message,
            tags = getTagsForLog(entity.tags),
            type = entity.type,
            createdAt = entity.createdAt,
            editedAt = entity.editedAt,
            reactions = reactionService.getLogReactions(entity.id),
            comments = getCommentsForLog(entity.id),
            author = profileService.getShortAccount(GetProfileDto(userId = entity.userId)),
        )
    }
    
    /**
     * Получение тегов для лога
     * ПОКА ЗАГЛУШКА
     */
    private fun getTagsForLog(tags: List<TagEntity>): List<TagDto> {
        return tags.map {
            TagDto(
                id = UUID.randomUUID(),
                name = "Яндекс",
                shortCompany = ShortCompanyDto(
                    companyId = UUID.randomUUID(),
                    name = "Яндекс",
                    avatarUrl = URI.create("https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Yandex_icon.svg/2048px-Yandex_icon.svg.png"),
                    primaryColor = ColorEnum.NAVY,
                    createdAt = OffsetDateTime.now()
                )
            )
        }
    }
    
    /**
     * Получение комментариев для лога
     * ПОКА ЗАГЛУШКА
     */
    private fun getCommentsForLog(logId: UUID): List<CommentDto> {
        return listOf(
            CommentDto(
                id = UUID.fromString("475da269-90ba-4ca6-88d1-6f1227dd6cb8"),
                message = "Комментарий",
                createdAt = OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now(),
                shortAccount = ShortAccountDto(
                    userId = UUID.randomUUID(),
                    fullName = "Эвилоныч",
                    avatarUrl = URI.create("https://sun9-25.userapi.com/s/v1/ig2/_BSpBnS-Zo2c2_J48KJk9POa1GDKa37nEJSdVoe-qeyNbIBoQmp4N4N6TtRIhr5xRvhB6O8VCBW2ke3jl9y2Y3NV.jpg?quality=96&as=32x43,48x64,72x96,108x144,160x214,240x320,360x480,480x641,540x721,640x854,720x961,959x1280&from=bu&u=o6kxoiUgTUztSx1nrI9fyiJnFMYWW64BuWCLXbMMpfc&cs=605x807")
                        .toString(),
                    roles = listOf(UserRole.DEANERY),
                    primaryColor = ColorEnum.NAVY,
                    email = "wtf@mail.ru",
                ),
                replyTo = null,
            ),
            CommentDto(
                id = UUID.fromString("953a4e9c-3a59-41d8-8083-36f6133c24d1"),
                message = "Комментарий",
                createdAt = OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now(),
                shortAccount = ShortAccountDto(
                    userId = UUID.randomUUID(),
                    fullName = "Подполковник Бустеренко",
                    avatarUrl = URI.create("https://super.ru/image/rs::3840:::/quality:90/plain/s3://super-static/prod/661faf8c06dba941afe9118a-1900x.jpeg")
                        .toString(),
                    roles = listOf(UserRole.STUDENT_SECOND),
                    primaryColor = ColorEnum.NAVY,
                    email = "wtf@mail.ru",
                ),
                replyTo = UUID.fromString("475da269-90ba-4ca6-88d1-6f1227dd6cb8"),
            ),
        )
    }
} 