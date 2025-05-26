package com.quqee.backend.internship_hits.marks.entity

import com.quqee.backend.internship_hits.public_interface.common.enums.DiaryStatusEnum
import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "mark")
data class MarkEntity(

    @Id
    @Column(name = "id", nullable = false)
    var id: UUID,

    @Column(name = "user_id", nullable = false)
    var userId: UUID,

    @Column(name = "mark", nullable = true)
    var mark: Int?,

    @Enumerated(EnumType.STRING)
    @Column(name = "diary_status", nullable = false)
    var diaryStatusEnum: DiaryStatusEnum = DiaryStatusEnum.NONE,

    @Column(name = "date", nullable = true)
    var date: OffsetDateTime?,

    @Column(name = "semester", nullable = false)
    var semester: Int,

    ) {
    constructor() : this(
        UUID.randomUUID(),
        UUID.randomUUID(),
        2,
        DiaryStatusEnum.NONE,
        OffsetDateTime.now(),
        2
    )
}
