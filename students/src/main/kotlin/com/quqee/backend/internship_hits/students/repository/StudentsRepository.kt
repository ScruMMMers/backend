package com.quqee.backend.internship_hits.students.repository

import com.quqee.backend.internship_hits.notification.public.tables.references.LOGS
import com.quqee.backend.internship_hits.notification.public.tables.references.LOG_POSITIONS
import com.quqee.backend.internship_hits.notification.public.tables.references.LOG_TAGS
import com.quqee.backend.internship_hits.notification.public.tables.references.POSITIONS
import com.quqee.backend.internship_hits.notification.public.tables.references.STUDENTS
import com.quqee.backend.internship_hits.notification.public.tables.references.TAGS
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationRequest
import com.quqee.backend.internship_hits.public_interface.common.SortingStrategy
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.students.entity.StudentEntity
import com.quqee.backend.internship_hits.students.entity.StudentEntityMapper
import com.quqee.backend.internship_hits.students.public_interface.CreateStudentDto
import org.jooq.*
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.time.Clock
import java.time.OffsetDateTime
import java.util.*

@Repository
class StudentsRepository(
    private val dsl: DSLContext,
    private val clock: Clock,
) {
    fun getStudents(
        pagination: LastIdPaginationRequest<UserId>,
        filter: StudentsFilterParams,
    ): Collection<StudentEntity> {
        val filteredStudentIds = if (pagination.lastId != null) {
            dsl.selectStudentIdsForList()
                .where(filter.toCondition())
                .groupBy(STUDENTS.USER_ID)
                .orderBy(STUDENTS.USER_ID.asc())
                .seek(pagination.lastId)
                .limit(pagination.sizeForSelect)
                .fetchInto(UUID::class.java)
        } else {
            dsl.selectStudentIdsForList()
                .where(filter.toCondition())
                .groupBy(STUDENTS.USER_ID)
                .orderBy(STUDENTS.USER_ID.asc())
                .limit(pagination.sizeForSelect)
                .fetchInto(UUID::class.java)
        }

        return dsl.selectStudentForList()
            .where(STUDENTS.USER_ID.`in`(filteredStudentIds))
            .orderBy(pagination.sorting.toOrderBy())
            .fetch()
            .groupBy { it[STUDENTS.USER_ID]!! }
            .values
            .mapNotNull { studentMapper(it, filter.withoutLogs) }
    }

    fun getFilteredStudentsSize(filter: StudentsFilterParams): Int {
        return dsl.fetchCount(dsl.selectStudentIdsForList()
            .where(filter.toCondition()))
    }

    fun getStudent(userId: UserId): StudentEntity? {
        return dsl.selectFrom(STUDENTS)
            .where(STUDENTS.USER_ID.eq(userId))
            .fetchOne(STUDENT_RECORD_MAPPER)
    }

    fun getStudentsByIds(userIds: Set<UserId>): List<StudentEntity> {
        return dsl.selectFrom(STUDENTS)
            .where(STUDENTS.USER_ID.`in`(userIds))
            .fetch(STUDENT_RECORD_MAPPER)
    }

    fun getStudentsByCourse(course: Int): List<StudentEntity> {
        return dsl.selectFrom(STUDENTS)
            .where(STUDENTS.STUDENT_COURSE.eq(course))
            .fetch(STUDENT_RECORD_MAPPER)
    }

    fun updateCourse(userIds: Set<UserId>, course: Int) {
        dsl.update(STUDENTS)
            .set(STUDENTS.STUDENT_COURSE, course)
            .where(STUDENTS.USER_ID.`in`(userIds))
            .execute()
    }

    fun updateStudent(entity: StudentEntity): StudentEntity {
        dsl.update(STUDENTS)
            .set(STUDENTS.STUDENT_COURSE, entity.course)
            .set(STUDENTS.STUDENT_GROUP, entity.group)
            .set(STUDENTS.IS_ON_ACADEMIC_LEAVE, entity.isOnAcademicLeave)
            .set(STUDENTS.COMPANY_ID, entity.companyId)
            .where(STUDENTS.USER_ID.eq(entity.userId))
            .execute()
        return entity
    }

    fun updateCompanyAndPosition(companyId: UUID, userId: UserId, positionId: Long?) {
        dsl.update(STUDENTS)
            .set(STUDENTS.COMPANY_ID, companyId)
            .set(STUDENTS.POSITION_ID, positionId)
            .where(STUDENTS.USER_ID.eq(userId))
            .execute()
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

    fun deleteStudent(userId: UserId) {
        dsl.deleteFrom(STUDENTS)
            .where(STUDENTS.USER_ID.eq(userId))
            .execute()
    }

    private fun studentMapper(records: Collection<Record>, withoutLogs: Boolean): StudentEntity? {
        val studentRecord = records.first()
        val logsRecords = records.filter { record -> record[LOGS.ID] != null }
            .groupBy { record -> record[LOGS.ID]!! }

        if (withoutLogs && logsRecords.isNotEmpty()) {
            return null
        }

        return StudentEntity(
            userId = studentRecord[STUDENTS.USER_ID]!!,
            course = studentRecord[STUDENTS.STUDENT_COURSE]!!,
            group = studentRecord[STUDENTS.STUDENT_GROUP]!!,
            isOnAcademicLeave = studentRecord[STUDENTS.IS_ON_ACADEMIC_LEAVE]!!,
            companyId = studentRecord[STUDENTS.COMPANY_ID],
            positionId = studentRecord[STUDENTS.POSITION_ID],
            logs = logsRecords.map { (_, logRecords) ->
                val baseRecord = logRecords.first()
                StudentEntity.StudentLog(
                    id = baseRecord[LOGS.ID]!!,
                    type = baseRecord[LOGS.TYPE]!!,
                    status = baseRecord[LOGS.APPROVAL_STATUS]!!,
                    createdAt = baseRecord[LOGS.CREATED_AT]!!,
                    companyIds = logRecords.mapNotNull { it[TAGS.COMPANY_ID] }.toSet(),
                )
            }.toSet(),
        )
    }

    private fun DSLContext.selectStudentIdsForList(): SelectOnConditionStep<Record1<UUID?>> {
        return this.selectDistinct(STUDENTS.USER_ID)
            .from(STUDENTS)
            .leftJoin(LOGS)
            .on(STUDENTS.USER_ID.eq(LOGS.USER_ID))
            .leftJoin(LOG_POSITIONS)
            .on(LOGS.ID.eq(LOG_POSITIONS.LOG_ID))
            .leftJoin(POSITIONS)
            .on(POSITIONS.ID.eq(LOG_POSITIONS.POSITION_ID))
            .leftJoin(LOG_TAGS)
            .on(LOGS.ID.eq(LOG_TAGS.LOG_ID))
            .leftJoin(TAGS)
            .on(LOG_TAGS.TAG_ID.eq(TAGS.ID))
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
            .leftJoin(LOG_TAGS)
            .on(LOGS.ID.eq(LOG_TAGS.LOG_ID))
            .leftJoin(TAGS)
            .on(LOG_TAGS.TAG_ID.eq(TAGS.ID))
    }

    private fun StudentsFilterParams.toCondition(): Collection<Condition> {
        val conditions = mutableListOf<Condition>()
        course?.let { conditions.add(STUDENTS.STUDENT_COURSE.`in`(it)) }
        group?.let { conditions.add(STUDENTS.STUDENT_GROUP.`in`(it)) }
        logType?.let { conditions.add(LOGS.TYPE.`in`(it)) }
        logApprovalStatus?.let { conditions.add(LOGS.APPROVAL_STATUS.`in`(it)) }
        positionType?.let { conditions.add(POSITIONS.POSITION.`in`(it)) }
        positionName?.let { conditions.add(POSITIONS.NAME.`in`(it)) }
        if (withoutCompanies) {
            conditions.add(STUDENTS.COMPANY_ID.isNull)
        } else {
            companyIds?.let { conditions.add(STUDENTS.COMPANY_ID.`in`(it)) }
        }
        userIds?.let { conditions.add(STUDENTS.USER_ID.`in`(it)) }

        if (!logByCompany.isNullOrEmpty()) {
            val typeIdPairs = mutableListOf<Condition>()
            logByCompany.forEach { (type, items) ->
                val companyIds = items.companyIds.orEmpty()
                if (companyIds.isNotEmpty()){
                    val companyIdPairs = companyIds.map { companyId ->
                        DSL.row(type.name, companyId)
                    }
                    conditions.add(DSL.row(LOGS.TYPE, TAGS.COMPANY_ID).`in`(companyIdPairs))
                }

                val positionTypes = items.positionTypes.orEmpty()
                if (positionTypes.isNotEmpty()){
                    val positionTypePairs = positionTypes.map { positionType ->
                        DSL.row(type.name, positionType.name)
                    }
                    conditions.add(DSL.row(LOGS.TYPE, POSITIONS.POSITION).`in`(positionTypePairs))
                }

                val positionNames = items.positionNames.orEmpty()
                if (positionNames.isNotEmpty()){
                    val positionNamePairs = positionNames.map { positionName ->
                        DSL.row(type.name, positionName)
                    }
                    conditions.add(DSL.row(LOGS.TYPE, POSITIONS.NAME).`in`(positionNamePairs))
                }

                if (companyIds.isEmpty() && positionTypes.isEmpty() && positionNames.isEmpty()) {
                    typeIdPairs.add(LOGS.TYPE.eq(type.name))
                }
            }

            conditions.addAll(typeIdPairs)
        }

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

                TAGS.ID,
                TAGS.COMPANY_ID,
            )
    }
}