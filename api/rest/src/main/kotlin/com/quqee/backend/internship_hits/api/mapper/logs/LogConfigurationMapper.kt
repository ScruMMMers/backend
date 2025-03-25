package com.quqee.backend.internship_hits.api.mapper.logs

import com.quqee.backend.internship_hits.mapper.*
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.public_interface.common.CommentDto
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.logs.LogDto
import com.quqee.backend.internship_hits.public_interface.reaction.ReactionDto
import com.quqee.backend.internship_hits.public_interface.common.TagDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI
import java.util.*

@Configuration
class LogConfigurationMapper (
    private val mapLogType: EnumerationMapper<LogTypeEnum, LogType>,
) {
    @Bean
    fun mapLog(
        mapTag: FromApiToInternalMapper<TagView, TagDto>,
        mapReaction: FromApiToInternalMapper<ReactionView, ReactionDto>,
        mapComment: FromApiToInternalMapper<CommentView, CommentDto>
    ): FromApiToInternalMapper<LogView, LogDto> = makeFromApiMapper { model ->
        LogDto(
            id = model.id,
            message = model.message,
            tags = model.tags.map { mapTag.fromApi(it) },
            type = mapLogType.mapToInternal(model.type),
            createdAt = model.createdAt,
            editedAt = model.editedAt,
            reactions = model.reactions.map { mapReaction.fromApi(it) },
            comments = model.comments.map { mapComment.fromApi(it) },
            author = ShortAccountDto(
                userId = UUID.randomUUID(),
                fullName = "Эвилоныч",
                avatarUrl = URI.create("https://sun9-25.userapi.com/s/v1/ig2/_BSpBnS-Zo2c2_J48KJk9POa1GDKa37nEJSdVoe-qeyNbIBoQmp4N4N6TtRIhr5xRvhB6O8VCBW2ke3jl9y2Y3NV.jpg?quality=96&as=32x43,48x64,72x96,108x144,160x214,240x320,360x480,480x641,540x721,640x854,720x961,959x1280&from=bu&u=o6kxoiUgTUztSx1nrI9fyiJnFMYWW64BuWCLXbMMpfc&cs=605x807")
                    .toString(),
                roles = listOf(UserRole.DEANERY),
                primaryColor = ColorEnum.NAVY,
                email = "wtf@mail.ru",
            )
        )
    }

    @Bean
    fun mapLogToApi(
        mapTag: FromInternalToApiMapper<TagView, TagDto>,
        mapReaction: FromInternalToApiMapper<ReactionView, ReactionDto>,
        mapComment: FromInternalToApiMapper<CommentView, CommentDto>
    ): FromInternalToApiMapper<LogView, LogDto> = makeToApiMapper { model ->
        LogView(
            id = model.id,
            message = model.message,
            tags = model.tags.map { mapTag.fromInternal(it) },
            type = mapLogType.mapToApi(model.type),
            createdAt = model.createdAt,
            editedAt = model.editedAt,
            reactions = model.reactions.map { mapReaction.fromInternal(it) },
            comments = model.comments.map { mapComment.fromInternal(it) }
        )
    }
}