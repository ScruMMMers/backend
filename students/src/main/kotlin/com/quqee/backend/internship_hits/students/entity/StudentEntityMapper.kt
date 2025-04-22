package com.quqee.backend.internship_hits.students.entity

import com.quqee.backend.internship_hits.notification.public.tables.records.StudentsRecord
import com.quqee.backend.internship_hits.notification.public.tables.references.STUDENTS
import org.jooq.RecordMapper

class StudentEntityMapper : RecordMapper<StudentsRecord, StudentEntity> {
    override fun map(record: StudentsRecord): StudentEntity {
        return StudentEntity(
            userId = record[STUDENTS.USER_ID]!!,
            course = record[STUDENTS.STUDENT_COURSE]!!,
            group = record[STUDENTS.STUDENT_GROUP]!!,
            isOnAcademicLeave = record[STUDENTS.IS_ON_ACADEMIC_LEAVE]!!,
            companyId = record[STUDENTS.COMPANY_ID],
            logs = emptySet(),
        )
    }
}