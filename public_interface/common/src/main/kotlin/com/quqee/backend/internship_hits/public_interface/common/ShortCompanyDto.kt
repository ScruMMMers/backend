package com.quqee.backend.internship_hits.public_interface.common

import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import java.net.URI
import java.time.OffsetDateTime
import java.util.UUID

data class ShortCompanyDto(
    val companyId: UUID,
    val name: String,
    val avatarUrl: URI,
    val primaryColor: ColorEnum,
    val createdAt: OffsetDateTime
) {
    constructor() : this(
        companyId = UUID.randomUUID(),
        name = "Яндекс",
        avatarUrl = URI.create("https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Yandex_icon.svg/2048px-Yandex_icon.svg.png"),
        primaryColor = ColorEnum.NAVY,
        createdAt = OffsetDateTime.now()
    )
}
