package com.quqee.backend.internship_hits.public_interface.company

import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import java.net.URI
import java.util.*

data class ShortCompanyWithEmployersDto(
    val companyId: UUID,
    val name: String,
    val avatarUrl: URI,
    val primaryColor: ColorEnum,
    val sinceYear: String,
    val description: String,
    val employedCount: Int,
) {
    constructor() : this(
        companyId = UUID.randomUUID(),
        name = "Яндекс",
        avatarUrl = URI.create("https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Yandex_icon.svg/2048px-Yandex_icon.svg.png"),
        primaryColor = ColorEnum.NAVY,
        sinceYear = "2000",
        description = "string",
        employedCount = 0,
    )
}
