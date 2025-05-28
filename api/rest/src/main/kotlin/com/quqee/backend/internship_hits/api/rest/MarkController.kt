package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.marks.service.MarkService
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.public_interface.mark.CreateMarkDto
import com.quqee.backend.internship_hits.public_interface.common.MarkDto
import com.quqee.backend.internship_hits.public_interface.common.MarkListDto
import com.quqee.backend.internship_hits.public_interface.common.StudentsMarksDto
import com.quqee.backend.internship_hits.public_interface.common.StudentsMarksListDto
import com.quqee.backend.internship_hits.public_interface.mark.CreateMarksListDto
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*
import javax.swing.SortOrder

@Component
class MarkController(
    private val markService: MarkService,
    private val mapMarkToApi: FromInternalToApiMapper<MarkView, MarkDto>,
    private val mapMarkListToApi: FromInternalToApiMapper<MarkListView, MarkListDto>,
    private val createMarkListToDto: FromApiToInternalMapper<CreateMarksListView, CreateMarksListDto>,
    private val mapStudentsMarksList: FromInternalToApiMapper<StudentsMarksListView, StudentsMarksListDto>,
) : MarksApiDelegate {

    override fun marksMyAllGet(): ResponseEntity<MarkListView> {
        return ResponseEntity.ok(
            mapMarkListToApi.fromInternal(markService.getMyMarks())
        )
    }

    override fun marksMyCurrentGet(): ResponseEntity<MarkView> {
        return ResponseEntity.ok(mapMarkToApi.fromInternal(markService.getMyCurrentMark()))
    }

    override fun marksUserIdCreatePost(userId: UUID, createMarksListView: CreateMarksListView): ResponseEntity<MarkListView> {
        val createMarkDto = createMarkListToDto.fromApi(createMarksListView)
        return ResponseEntity.ok(mapMarkListToApi.fromInternal(markService.saveMarks(userId, createMarkDto)))
    }

    override fun marksStudentsListGet(
        search: String?,
        semester: Int?,
        diaryDoneFirst: Boolean?,
        diaryStatus: DiaryStatusEnum?,
        mark: Int?,
        orderByGroup: OrderStrategy?,
        lastId: UUID?,
        size: Int?
    ): ResponseEntity<StudentsMarksListView> {
        var status: com.quqee.backend.internship_hits.public_interface.common.enums.DiaryStatusEnum? = null
        if (diaryStatus != null) {
            status =
                com.quqee.backend.internship_hits.public_interface.common.enums.DiaryStatusEnum.valueOf(diaryStatus.toString())
        }

        var order: SortOrder? = null
        if (orderByGroup != null) {
            order = SortOrder.valueOf(orderByGroup.value)
        }

        return ResponseEntity.ok(
            mapStudentsMarksList.fromInternal(
                markService.getStudentsMarksList(
                    search,
                    semester,
                    diaryDoneFirst,
                    status,
                    mark,
                    order,
                    lastId,
                    size
                )
            )
        )
    }

}