package com.quqee.backend.internship_hits.public_interface.mark

import com.quqee.backend.internship_hits.public_interface.common.enums.DiaryStatusEnum
import java.time.OffsetDateTime
import java.util.UUID

data class MarkDto(
    val id: UUID,
    val userId: UUID,
    val mark: Int?,
    val diary: DiaryStatusEnum,
    val date: OffsetDateTime?,
    val semester: Int,
)
