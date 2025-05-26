package com.quqee.backend.internship_hits.public_interface.common

import com.quqee.backend.internship_hits.public_interface.common.enums.DiaryStatusEnum
import java.util.UUID

interface StudentsMarksProjection {
    val id: UUID
    val group: String
    val course: Int
    val fifthSemesterDiary: DiaryStatusEnum
    val fifthSemesterMark: Int?
    val sixthSemesterDiary: DiaryStatusEnum
    val sixthSemesterMark: Int?
    val seventhSemesterDiary: DiaryStatusEnum
    val seventhSemesterMark: Int?
    val eighthSemesterDiary: DiaryStatusEnum
    val eighthSemesterMark: Int?
}
