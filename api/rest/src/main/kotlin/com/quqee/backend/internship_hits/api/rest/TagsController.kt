package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.model.rest.ShortCompanyView
import com.quqee.backend.internship_hits.model.rest.TagView
import com.quqee.backend.internship_hits.model.rest.TagsListView
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.net.URI
import java.util.*

@Component
class TagsController : TagsApiDelegate {
    override fun tagsGet(name: String?): ResponseEntity<TagsListView> {
        return ResponseEntity.ok(
            TagsListView(
                tags = listOf(
                    TagView(
                        id = UUID.randomUUID(),
                        name = "Яндекс",
                        shortCompany = ShortCompanyView(
                            companyId = UUID.randomUUID(),
                            name = "Яндекс",
                            avatarUrl = URI.create("https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Yandex_icon.svg/2048px-Yandex_icon.svg.png"),
                            primaryColor = "#ef1818",
                        )
                    )
                )
            )
        )
    }
}