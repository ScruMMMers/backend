package com.quqee.backend.internship_hits.students.repository

import com.quqee.backend.internship_hits.notification.public.tables.references.LOGS
import com.quqee.backend.internship_hits.notification.public.tables.references.LOG_POSITIONS
import com.quqee.backend.internship_hits.notification.public.tables.references.POSITIONS
import com.quqee.backend.internship_hits.notification.public.tables.references.STUDENTS
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.SortingStrategy
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.students.entity.StudentEntity
import com.quqee.backend.internship_hits.students.entity.StudentEntityMapper
import com.quqee.backend.internship_hits.students.public_interface.CreateStudentDto
import org.jooq.*
import org.springframework.stereotype.Repository
import java.time.Clock
import java.time.OffsetDateTime

@Repository
class StudentsRepository(
    private val dsl: DSLContext,
    private val clock: Clock,
) {
    fun getStudents(
        pagination: LastIdPaginationRequest<UserId>,
        filter: StudentsFilterParams,
    ): Collection<StudentEntity> {
        return if (pagination.lastId != null) {

            //Костыль, чтоб не падал запрос
            val lastStudent = dsl.select(STUDENTS.CREATED_AT)
                .from(STUDENTS)
                .where(STUDENTS.USER_ID.eq(pagination.lastId))
                .fetchOne()

            if (lastStudent == null) {
                return emptyList()
            }

            val lastCreatedAt = lastStudent.get(STUDENTS.CREATED_AT)

            dsl.selectStudentForList()
                .where(filter.toCondition())
                .and(STUDENTS.CREATED_AT.gt(lastCreatedAt))
                .orderBy(pagination.sorting.toOrderBy())
                .limit(pagination.sizeForSelect)
                .fetch()
        } else {
            dsl.selectStudentForList()
                .where(filter.toCondition())
                .orderBy(pagination.sorting.toOrderBy())
                .limit(pagination.sizeForSelect)
                .fetch()
        }
            .groupBy { it[STUDENTS.USER_ID]!! }
            .values
            .map(::studentMapper)
    }

    fun getStudent(userId: UserId): StudentEntity? {
        return dsl.selectFrom(STUDENTS)
            .where(STUDENTS.USER_ID.eq(userId))
            .fetchOne(STUDENT_RECORD_MAPPER)
    }

    fun updateStudent(entity: StudentEntity): StudentEntity {
        dsl.update(STUDENTS)
            .set(STUDENTS.STUDENT_COURSE, entity.course)
            .set(STUDENTS.STUDENT_GROUP, entity.group)
            .set(STUDENTS.IS_ON_ACADEMIC_LEAVE, entity.isOnAcademicLeave)
            .set(STUDENTS.COMPANY_ID, entity.companyId)
            .execute()
        return entity
    }

    fun createStudent(dto: CreateStudentDto): StudentEntity {
        return dsl.insertInto(STUDENTS)
            .set(STUDENTS.USER_ID, dto.userId)
            .set(STUDENTS.STUDENT_COURSE, dto.course)
            .set(STUDENTS.STUDENT_GROUP, dto.group)
            .set(STUDENTS.IS_ON_ACADEMIC_LEAVE, dto.isOnAcademicLeave)
            .set(STUDENTS.COMPANY_ID, dto.companyId)
            .set(STUDENTS.CREATED_AT, OffsetDateTime.now(clock))
            .returning(StudentEntity.FIELDS)
            .fetchOne(STUDENT_RECORD_MAPPER)!!
    }

    private fun studentMapper(records: Collection<Record>): StudentEntity {
        val studentRecord = records.first()
        return StudentEntity(
            userId = studentRecord[STUDENTS.USER_ID]!!,
            course = studentRecord[STUDENTS.STUDENT_COURSE]!!,
            group = studentRecord[STUDENTS.STUDENT_GROUP]!!,
            isOnAcademicLeave = studentRecord[STUDENTS.IS_ON_ACADEMIC_LEAVE]!!,
            companyId = studentRecord[STUDENTS.COMPANY_ID],
            logs = records.map { record ->
                StudentEntity.StudentLog(
                    id = record[LOGS.ID]!!,
                    type = record[LOGS.TYPE]!!,
                    status = record[LOGS.APPROVAL_STATUS]!!
                )
            }.toSet(),
        )
    }

    private fun DSLContext.selectStudentForList(): SelectOnConditionStep<Record> {
        return this.select(SELECTED_FIELDS_FOR_LIST)
            .from(STUDENTS)
            .leftJoin(LOGS)
            .on(STUDENTS.USER_ID.eq(LOGS.USER_ID))
            .leftJoin(LOG_POSITIONS)
            .on(LOGS.ID.eq(LOG_POSITIONS.LOG_ID))
            .leftJoin(POSITIONS)
            .on(POSITIONS.ID.eq(LOG_POSITIONS.POSITION_ID))
    }

    private fun StudentsFilterParams.toCondition(): Collection<Condition> {
        val conditions = mutableListOf<Condition>()
        course?.let { conditions.add(STUDENTS.STUDENT_COURSE.`in`(it)) }
        group?.let { conditions.add(STUDENTS.STUDENT_GROUP.`in`(it)) }
        logType?.let { conditions.add(LOGS.TYPE.`in`(it)) }
        logApprovalStatus?.let { conditions.add(LOGS.APPROVAL_STATUS.`in`(it)) }
        positionType?.let { conditions.add(POSITIONS.POSITION.`in`(it)) }
        positionName?.let { conditions.add(POSITIONS.NAME.`in`(it)) }
        return conditions
    }

    private fun SortingStrategy.toOrderBy(): List<SortField<*>> {
        return when (this) {
            SortingStrategy.CREATED_AT_ASC -> listOf(STUDENTS.CREATED_AT.asc())
            SortingStrategy.CREATED_AT_DESC -> listOf(STUDENTS.CREATED_AT.desc())
        }
    }

    companion object {
        private val STUDENT_RECORD_MAPPER = StudentEntityMapper()
        private val SELECTED_FIELDS_FOR_LIST =
            StudentEntity.FIELDS + listOf(
                POSITIONS.ID,
                POSITIONS.NAME,
                POSITIONS.POSITION,

                LOG_POSITIONS.LOG_ID,
                LOG_POSITIONS.POSITION_ID,

                LOGS.ID,
                LOGS.USER_ID,
                LOGS.TYPE,
                LOGS.APPROVAL_STATUS,
            )
    }
}