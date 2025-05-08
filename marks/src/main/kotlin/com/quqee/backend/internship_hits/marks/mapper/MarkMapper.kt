package com.quqee.backend.internship_hits.marks.mapper

import com.quqee.backend.internship_hits.marks.entity.MarkEntity
import com.quqee.backend.internship_hits.public_interface.mark.MarkDto
import org.springframework.stereotype.Component

@Component
class MarkMapper {

    fun mapToDto(markEntity: MarkEntity): MarkDto {
        return MarkDto(
            markEntity.id,
            markEntity.userId,
            markEntity.mark,
            markEntity.date,
            markEntity.semester,
        )
    }

}