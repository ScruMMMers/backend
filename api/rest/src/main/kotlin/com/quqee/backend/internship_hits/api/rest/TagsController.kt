package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.TagsListView
import com.quqee.backend.internship_hits.public_interface.tags.TagListDto
import com.quqee.backend.internship_hits.tags_query.service.TagQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class TagsController(
    private val tagsQueryService: TagQueryService,
    private val mapTagsList: FromInternalToApiMapper<TagsListView, TagListDto>
) : TagsApiDelegate {
    override fun tagsGet(name: String?): ResponseEntity<TagsListView> {
        val tagsListDto = tagsQueryService.getTagsByNamePart(name)
        return ResponseEntity.ok(mapTagsList.fromInternal(tagsListDto))
    }
}
