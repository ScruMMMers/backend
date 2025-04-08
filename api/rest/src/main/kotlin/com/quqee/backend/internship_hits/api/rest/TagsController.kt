package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.PositionListView
import com.quqee.backend.internship_hits.model.rest.PositionView
import com.quqee.backend.internship_hits.model.rest.TagsListView
import com.quqee.backend.internship_hits.position.service.PositionService
import com.quqee.backend.internship_hits.public_interface.common.PositionDto
import com.quqee.backend.internship_hits.public_interface.tags.TagListDto
import com.quqee.backend.internship_hits.tags_query.service.TagQueryService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class TagsController(
    private val tagsQueryService: TagQueryService,
    private val positionService: PositionService,
    private val mapTagsList: FromInternalToApiMapper<TagsListView, TagListDto>,
    private val mapPosition: FromInternalToApiMapper<PositionView, PositionDto>,
) : TagsApiDelegate {
    override fun tagsGet(name: String?): ResponseEntity<TagsListView> {
        val tagsListDto = tagsQueryService.getTagsByNamePart(name)
        return ResponseEntity.ok(mapTagsList.fromInternal(tagsListDto))
    }

    override fun hashtagsGet(name: String?): ResponseEntity<PositionListView> {
        val hashtagsList = positionService.getPositionListByPartName(name).map { mapPosition.fromInternal(it) }
        return ResponseEntity.ok(PositionListView(hashtagsList))
    }
}
