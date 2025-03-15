package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.model.rest.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.net.URI
import java.time.OffsetDateTime
import java.util.*

@Component
class LogsController : LogsApiDelegate {
    private val logView = LogView(
        id = UUID.randomUUID(),
        message = "",
        tags = listOf(
            TagView(
                id = UUID.randomUUID(),
                name = "Яндекс",
                shortCompany = ShortCompanyView(
                    companyId = UUID.randomUUID(),
                    name = "Яндекс",
                    avatarUrl = URI.create("https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Yandex_icon.svg/2048px-Yandex_icon.svg.png"),
                    primaryColor = "#ef1818",
                )
            )
        ),
        type = LogTypeEnum.DEFAULT,
        createdAt = OffsetDateTime.now(),
        editedAt = OffsetDateTime.now(),
        reactions = listOf(
            ReactionView(
                shortAccount = ShortAccountView(
                    userId = UUID.randomUUID(),
                    fullName = "Иван Иванов",
                    avatarUrl = URI.create("https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Yandex_icon.svg/2048px-Yandex_icon.svg.png"),
                    roles = listOf(RoleEnum.DEANERY),
                    primaryColor = "#533af9"
                ),
                emoji = "\uD83D\uDE33"
            )
        ),
        comments = listOf(
            CommentView(
                id = UUID.fromString("475da269-90ba-4ca6-88d1-6f1227dd6cb8"),
                message = "Комментарий",
                createdAt = OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now(),
                shortAccount = ShortAccountView(
                    userId = UUID.randomUUID(),
                    fullName = "Эвилоныч",
                    avatarUrl = URI.create("https://sun9-25.userapi.com/s/v1/ig2/_BSpBnS-Zo2c2_J48KJk9POa1GDKa37nEJSdVoe-qeyNbIBoQmp4N4N6TtRIhr5xRvhB6O8VCBW2ke3jl9y2Y3NV.jpg?quality=96&as=32x43,48x64,72x96,108x144,160x214,240x320,360x480,480x641,540x721,640x854,720x961,959x1280&from=bu&u=o6kxoiUgTUztSx1nrI9fyiJnFMYWW64BuWCLXbMMpfc&cs=605x807"),
                    roles = listOf(RoleEnum.DEANERY),
                    primaryColor = "#533af9"
                ),
                replyTo = null,
            ),
            CommentView(
                id = UUID.fromString("953a4e9c-3a59-41d8-8083-36f6133c24d1"),
                message = "Комментарий",
                createdAt = OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now(),
                shortAccount = ShortAccountView(
                    userId = UUID.randomUUID(),
                    fullName = "Подполковник Бустеренко",
                    avatarUrl = URI.create("https://super.ru/image/rs::3840:::/quality:90/plain/s3://super-static/prod/661faf8c06dba941afe9118a-1900x.jpeg"),
                    roles = listOf(RoleEnum.STUDENT_SECOND),
                    primaryColor = "#ff8fe9"
                ),
                replyTo = UUID.fromString("475da269-90ba-4ca6-88d1-6f1227dd6cb8"),
            ),
        )
    )

    override fun logsLogIdPost(
        logId: UUID,
        updateLogRequestView: UpdateLogRequestView
    ): ResponseEntity<CreatedLogView> {
        return ResponseEntity.ok(
            CreatedLogView(log = logView)
        )
    }

    override fun logsMeGet(lastId: Int?, size: Int?): ResponseEntity<LogsListView> {
        return ResponseEntity.ok(
            LogsListView(
                logs = listOf(logView),
                page = LastIdPaginationView(
                    lastId = UUID.randomUUID().toString(),
                    pageSize = 1,
                    hasNext = false
                )
            )
        )
    }

    override fun logsPost(createLogRequestView: CreateLogRequestView): ResponseEntity<CreatedLogView> {
        return ResponseEntity.ok(
            CreatedLogView(
                log = logView
            )
        )
    }

    override fun logsUserIdGet(userId: UUID, lastId: Int?, size: Int?): ResponseEntity<LogsListView> {
        return ResponseEntity.ok(
            LogsListView(
                logs = listOf(logView),
                page = LastIdPaginationView(
                    lastId = UUID.randomUUID().toString(),
                    pageSize = 1,
                    hasNext = false
                )
            )
        )
    }
}