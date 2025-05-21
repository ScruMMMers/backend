package com.quqee.backend.internship_hits.marks.mapper

import com.quqee.backend.internship_hits.marks.entity.MarkEntity
import com.quqee.backend.internship_hits.public_interface.common.enums.DiaryStatusEnum
import com.quqee.backend.internship_hits.public_interface.mark.MarkDto
import com.quqee.backend.internship_hits.public_interface.mark.MarkListDto
import org.springframework.stereotype.Component

@Component
class MarkMapper {

    fun mapToDto(markEntity: MarkEntity, diaryStatusEnum: DiaryStatusEnum): MarkDto {
        return MarkDto(
            markEntity.id,
            markEntity.userId,
            markEntity.mark,
            diaryStatusEnum,
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