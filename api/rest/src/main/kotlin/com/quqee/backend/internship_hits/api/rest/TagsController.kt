package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.model.rest.TagsListView
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class TagsController : TagsApiDelegate {
    override fun tagsGet(name: String?): ResponseEntity<TagsListView> {
        return super.tagsGet(name)
    }
}