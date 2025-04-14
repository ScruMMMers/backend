package com.quqee.backend.internship_hits.students.entity

import com.quqee.backend.internship_hits.notification.public.tables.references.STUDENTS
import com.quqee.backend.internship_hits.public_interface.common.UserId
import java.util.*

data class StudentEntity(
    val userId: UserId,
    val course: Int,
    val group: String,
    val isOnAcademicLeave: Boolean,
    val companyId: UUID?,
) {
    companion object {
        val FIELDS = listOf(
            STUDENTS.USER_ID,
            STUDENTS.STUDENT_COURSE,
            STUDENTS.STUDENT_GROUP,
            STUDENTS.IS_ON_ACADEMIC_LEAVE,
            STUDENTS.CREATED_AT,
            STUDENTS.COMPANY_ID,
        )
    }
}
