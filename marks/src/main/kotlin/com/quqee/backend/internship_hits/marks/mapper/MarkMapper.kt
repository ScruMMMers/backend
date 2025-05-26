package com.quqee.backend.internship_hits.marks.mapper

import com.quqee.backend.internship_hits.marks.entity.MarkEntity
import com.quqee.backend.internship_hits.public_interface.common.enums.DiaryStatusEnum
import com.quqee.backend.internship_hits.public_interface.common.MarkDto
import com.quqee.backend.internship_hits.public_interface.common.MarkListDto
import org.springframework.stereotype.Component

@Component
class MarkMapper {

    fun mapToDto(markEntity: MarkEntity): MarkDto {
        return MarkDto(
            markEntity.id,
            markEntity.userId,
            markEntity.mark,
            markEntity.diaryStatusEnum,
            markEntity.date,
            markEntity.semester,
        )
    }

    fun mapToListDto(marks: List<MarkDto>): MarkListDto {
        return MarkListDto(
            marks
        )
    }

}