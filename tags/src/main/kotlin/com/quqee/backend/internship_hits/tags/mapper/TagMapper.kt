package com.quqee.backend.internship_hits.tags.mapper

import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.tags.TagDto
import com.quqee.backend.internship_hits.public_interface.tags.TagListDto
import com.quqee.backend.internship_hits.tags.entity.TagEntity
import org.springframework.stereotype.Service
import java.net.URI
import java.util.*

@Service
class TagMapper {
    fun toTagDto(tagEntity: TagEntity): TagDto {
        return TagDto(
            id = tagEntity.id,
            shortCompany = ShortCompanyDto(
                companyId = UUID.randomUUID(),
                name = "Яндекс",
                avatarUrl = URI.create("https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Yandex_icon.svg/2048px-Yandex_icon.svg.png"),
                primaryColor = "#ef1818",
            ),
            name = tagEntity.name
        )
    }

    fun toTagListDto(tagEntities: List<TagEntity>): TagListDto {
        val tags = tagEntities.map { toTagDto(it) }
        return TagListDto(tags = tags)
    }
}
