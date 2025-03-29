package com.quqee.backend.internship_hits.tags_query.mapper

import com.quqee.backend.internship_hits.company.service.CompanyService
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import com.quqee.backend.internship_hits.public_interface.common.TagDto
import com.quqee.backend.internship_hits.public_interface.tags.TagListDto
import com.quqee.backend.internship_hits.tags.entity.TagEntity
import org.springframework.stereotype.Service
import java.net.URI
import java.time.OffsetDateTime
import java.util.*

@Service
class TagQueryMapper(
    private val companyService: CompanyService,
) {
    fun toTagDto(tagEntity: TagEntity): TagDto {
        return TagDto(
            id = tagEntity.id,
            shortCompany = companyService.getShortCompany(companyId = tagEntity.companyId) ?: ShortCompanyDto(),
            name = tagEntity.name
        )
    }

    fun toTagListDto(tagEntities: List<TagEntity>): TagListDto {
        val tags = tagEntities.map { toTagDto(it) }
        return TagListDto(tags = tags)
    }
}
