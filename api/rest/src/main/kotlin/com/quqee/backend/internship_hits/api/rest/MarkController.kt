package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.marks.service.MarkService
import com.quqee.backend.internship_hits.model.rest.CreateMarkView
import com.quqee.backend.internship_hits.model.rest.MarkListView
import com.quqee.backend.internship_hits.model.rest.MarkView
import com.quqee.backend.internship_hits.public_interface.mark.CreateMarkDto
import com.quqee.backend.internship_hits.public_interface.mark.MarkDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class MarkController(
    private val markService: MarkService,
    private val mapMarkToApi: FromInternalToApiMapper<MarkView, MarkDto>,
    private val createMarkToDto: FromApiToInternalMapper<CreateMarkView, CreateMarkDto>,
) : MarksApiDelegate {

    override fun marksMyAllGet(): ResponseEntity<MarkListView> {
        return ResponseEntity.ok(MarkListView(markService.getMyMarks().map { mapMarkToApi.fromInternal(it) }))
    }

    override fun marksMyCurrentGet(): ResponseEntity<MarkView> {
        return ResponseEntity.ok(mapMarkToApi.fromInternal(markService.getMyCurrentMark()))
    }

    override fun marksUserIdCreatePost(userId: UUID, createMarkView: CreateMarkView): ResponseEntity<MarkView> {

        val createMarkDto = createMarkToDto.fromApi(createMarkView)
        return ResponseEntity.ok(mapMarkToApi.fromInternal(markService.createMark(userId, createMarkDto)))
    }

}