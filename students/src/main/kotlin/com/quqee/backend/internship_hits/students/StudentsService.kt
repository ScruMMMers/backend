package com.quqee.backend.internship_hits.students

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.quqee.backend.internship_hits.company.service.CompanyService
import com.quqee.backend.internship_hits.file.service.FileService
import com.quqee.backend.internship_hits.notification.service.NotificationService
import com.quqee.backend.internship_hits.position.service.PositionService
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.profile.dto.CreateUserDto
import com.quqee.backend.internship_hits.public_interface.common.GetProfileDto
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationResponse
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.enums.UserRole
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.logs.ShortLogInfo
import com.quqee.backend.internship_hits.public_interface.notification_public.CreateNotificationDto
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationChannel
import com.quqee.backend.internship_hits.public_interface.notification_public.NotificationType
import com.quqee.backend.internship_hits.public_interface.students_public.*
import com.quqee.backend.internship_hits.students.dtos.StudentFromFileDto
import com.quqee.backend.internship_hits.students.entity.InviteLinkConfigEntity
import com.quqee.backend.internship_hits.students.entity.InviteLinkEntity
import com.quqee.backend.internship_hits.students.entity.StudentEntity
import com.quqee.backend.internship_hits.students.public_interface.CreateInviteLinkDto
import com.quqee.backend.internship_hits.students.public_interface.CreateStudentDto
import com.quqee.backend.internship_hits.students.public_interface.CreateStudentsFromFileDto
import com.quqee.backend.internship_hits.students.repository.InviteLinkRepository
import com.quqee.backend.internship_hits.students.repository.StudentsFilterParams
import com.quqee.backend.internship_hits.students.repository.StudentsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.CollectionUtils
import org.springframework.web.util.UriComponentsBuilder
import java.util.*
import com.quqee.backend.internship_hits.public_interface.students_public.CreateInviteLinkDto as ExternalCreateInviteLinkDto
import com.quqee.backend.internship_hits.public_interface.students_public.CreateStudentDto as ExternalCreateStudentDto
import com.quqee.backend.internship_hits.public_interface.students_public.DeaneryEditStudentDto as ExternalDeaneryEditStudentDto

@Service
class StudentsService(
    private val profileService: ProfileService,
    private val companyService: CompanyService,
    private val positionService: PositionService,
    private val notificationService: NotificationService,
    private val fileService: FileService,
    private val studentsRepository: StudentsRepository,
    private val inviteLinkRepository: InviteLinkRepository,
    @Value("\${client.uri}")
    private val clientUri: String,
) {
    fun getStudentsList(dto: GetStudentsListDto): LastIdPaginationResponse<StudentDto, UserId> {
        val userIds = dto.filter.name?.let { name ->
            profileService.getUserIdsByName(name)
        }

        val filterDto = StudentsFilterParams(
            course = dto.filter.course,
            group = dto.filter.group,
            logType = dto.filter.logType,
            logApprovalStatus = dto.filter.logApprovalStatus,
            positionType = dto.filter.positionType,
            positionName = dto.filter.positionName,
            companyIds = dto.filter.companyIds,
            logByCompany = dto.filter.logByCompany,
            userIds = userIds,
            withoutCompanies = dto.filter.withoutCompanies,
            withoutLogs = dto.filter.withoutLogs,
        )
        val students = studentsRepository.getStudents(
            pagination = dto.pagination,
            filter = filterDto,
        )

        val idOrder = students.withIndex().associate { it.value.userId to it.index }
        val studentDtos = runBlocking {
            val deferred = students.map { studentEntity ->
                async {
                    mapStudentToDto(studentEntity)
                }
            }
            deferred.awaitAll()
        }.sortedBy { idOrder[it.id] }

        return LastIdPaginationResponse(
            studentDtos,
            dto.pagination,
            studentsRepository.getFilteredStudentsSize(filterDto)
        )
    }

    fun getStudent(id: UUID): StudentDto {
        val student = studentsRepository.getStudent(id)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND)
        return mapStudentToDto(student)
    }

    fun createInviteLink(dto: ExternalCreateInviteLinkDto): InviteLinkDto {
        val createDto = CreateInviteLinkDto(
            config = InviteLinkConfigEntity(
                group = dto.group,
                course = dto.course,
            ),
        )
        val inviteLink = inviteLinkRepository.createLink(createDto)
        return createInviteLinkDto(inviteLink)
    }

    @Transactional
    fun importStudents(fileId: UUID) {
        val studentsDtos = getStudentsForCreate(fileId)

        val students = runBlocking {
            val deferred = studentsDtos.map { dto ->
                async {
                    val password = UUID.randomUUID().toString().take(8)
                    val login = dto.email.substringBefore("@")

                    val userId = profileService.createProfile(
                        CreateUserDto(
                            username = login,
                            email = dto.email,
                            firstName = dto.firstName,
                            lastName = dto.lastName,
                            password = password,
                            roles = getRoleByCourse(dto.course),
                            middleName = dto.middleName,
                            photoId = null,
                        )
                    )
                    studentsRepository.createStudent(
                        CreateStudentDto(
                            userId = userId,
                            course = dto.course,
                            group = dto.group,
                            isOnAcademicLeave = false,
                            companyId = null,
                        )
                    )

                    StudentFromFileDto(
                        userId = userId,
                        password = password,
                        login = login,
                    )
                }
            }
            deferred.awaitAll()
        }

        notificationService.createNotifications(
            students.map { dto ->
                CreateNotificationDto(
                    title = "Временный пароль",
                    message = "Для входа в систему используйте временный пароль: ${dto.password}; логин: ${dto.login}",
                    userId = dto.userId,
                    channels = setOf(
                        NotificationChannel.EMAIL,
                    ),
                    type = NotificationType.SYSTEM,
                )
            }
        )
    }

    private fun getStudentsForCreate(
        fileId: UUID,
    ): List<CreateStudentsFromFileDto> {
        val fileStream = fileService.getFileInputStream(fileId)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Файл не найден")
        return fileStream.use { file ->
            XSSFWorkbook(file).use { workbook ->
                val sheet = workbook.getSheetAt(0)

                val headerRow = sheet.getRow(0)
                val columnIndexes = mutableMapOf<String, Int>()
                headerRow.forEach { cell ->
                    val columnName = cell.stringCellValue
                    columnIndexes[columnName] = cell.columnIndex
                }

                val students = mutableListOf<CreateStudentsFromFileDto>()
                for (i in 1..sheet.lastRowNum) {
                    val row = sheet.getRow(i) ?: continue

                    val email = getCellValueAsString(row.getCell(columnIndexes["Почта"] ?: -1))
                        ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Почта не указана")
                    val firstName = getCellValueAsString(row.getCell(columnIndexes["Имя"] ?: -1))
                        ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Имя не указано")
                    val lastName = getCellValueAsString(row.getCell(columnIndexes["Фамилия"] ?: -1))
                        ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Фамилия не указана")
                    val course = getCellValueAsString(row.getCell(columnIndexes["Курс"] ?: -1))
                        ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Курс не указан или некорректен")
                    val group = getCellValueAsString(row.getCell(columnIndexes["Группа"] ?: -1))
                        ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Группа не указана")
                    val middleName = getCellValueAsString(row.getCell(columnIndexes["Отчество"] ?: -1))
                        .takeIf { !it.isNullOrEmpty() }

                    students.add(
                        CreateStudentsFromFileDto(
                            email = email,
                            firstName = firstName,
                            lastName = lastName,
                            course = course.toInt(),
                            group = group,
                            middleName = middleName
                        )
                    )
                }
                students
            }
        }
    }

    private fun getCellValueAsString(cell: Cell?): String? {
        if (cell == null) return null
        return when (cell.cellType) {
            CellType.STRING -> cell.stringCellValue.trim()
            CellType.NUMERIC -> {
                val value = cell.numericCellValue
                if (value == value.toLong().toDouble()) {
                    value.toLong().toString()
                } else {
                    value.toString().trim()
                }
            }
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            CellType.FORMULA -> getCellValueAsString(
                when (cell.cachedFormulaResultType) {
                    CellType.STRING -> cell
                    CellType.NUMERIC -> cell
                    else -> cell
                }
            )
            else -> cell.toString().trim()
        }
    }

    @Transactional
    fun createStudent(dto: ExternalCreateStudentDto): StudentDto {
        val inviteLink = inviteLinkRepository.getLink(dto.inviteLinkId)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Пригласительная ссылка не найдена")

        val userId = profileService.createProfile(
            CreateUserDto(
                username = dto.username,
                email = dto.email,
                firstName = dto.firstName,
                lastName = dto.lastName,
                password = dto.password,
                roles = getRoleByCourse(dto.course),
                middleName = dto.middleName,
                photoId = dto.photoId,
            )
        )
        val student = studentsRepository.createStudent(
            CreateStudentDto(
                userId = userId,
                course = inviteLink.config.course,
                group = inviteLink.config.group ?: dto.group,
                isOnAcademicLeave = false,
                companyId = null,
            )
        )
        return mapStudentToDto(student)
    }

    @Transactional
    fun moveStudentToAcademic(userId: UserId): StudentDto {
        val studentEntity = studentsRepository.getStudent(userId)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Студент не найден")
        if (studentEntity.isOnAcademicLeave) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Студент уже находится в академе")
        }

        val updatedStudent = studentEntity.copy(isOnAcademicLeave = true)
        return mapStudentToDto(studentsRepository.updateStudent(updatedStudent))
    }

    @Transactional
    fun removeStudentFromAcademic(userId: UserId): StudentDto {
        val studentEntity = studentsRepository.getStudent(userId)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Студент не найден")
        if (!studentEntity.isOnAcademicLeave) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Студент и так не в академе")
        }

        val updatedStudent = studentEntity.copy(isOnAcademicLeave = false)
        return mapStudentToDto(studentsRepository.updateStudent(updatedStudent))
    }

    @Transactional
    fun moveToCourse(dto: MoveToCourseDto) {
        when (dto) {
            is MoveToCourseByCourseDto -> {
                val students = studentsRepository.getStudentsByCourse(dto.fromCourse)
                val userIds = students.map { it.userId }.toSet()

                val fromCourseRole = getRoleByCourse(dto.fromCourse)
                val toCourseRole = getRoleByCourse(dto.toCourse)

                profileService.removeRoles(userIds, fromCourseRole)
                profileService.addRoles(userIds, toCourseRole)
                studentsRepository.updateCourse(userIds, dto.toCourse)
            }
            is MoveToCourseByUserDto -> {
                val students = studentsRepository.getStudentsByIds(dto.userIds)
                val toCourseRole = getRoleByCourse(dto.toCourse)

                students.forEach { student ->
                    val userIds = setOf(student.userId)
                    profileService.removeRoles(userIds, getRoleByCourse(student.course))
                    profileService.addRoles(userIds, toCourseRole)
                    studentsRepository.updateCourse(userIds, dto.toCourse)
                }
            }
            is MoveToCourseAllDto -> {
                (1..4).map { course ->
                    course to studentsRepository.getStudentsByCourse(course).map { it.userId }.toSet()
                }.forEach { (fromCourse, studentsIds) ->
                    val toCourse = fromCourse + 1
                    val fromCourseRole = getRoleByCourse(fromCourse)
                    val toCourseRole = getRoleByCourse(toCourse)

                    profileService.removeRoles(studentsIds, fromCourseRole)
                    profileService.addRoles(studentsIds, toCourseRole)
                    studentsRepository.updateCourse(studentsIds, toCourse)
                }
            }
        }
    }

    @Transactional
    fun deaneryEditStudent(dto: ExternalDeaneryEditStudentDto): StudentDto {
        val student = studentsRepository.getStudent(dto.studentId)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Студент не найден")
        val oldCourse = student.course
        val newCourse = dto.course

        val updateStudent = student.copy(
            course = dto.course,
            group = dto.group,
            companyId = dto.companyId,
        )
        val updatedStudent = studentsRepository.updateStudent(updateStudent)

        if (oldCourse != newCourse){
            val fromCourseRole = getRoleByCourse(oldCourse)
            val toCourseRole = getRoleByCourse(newCourse)

            profileService.removeRoles(setOf(dto.studentId), fromCourseRole)
            profileService.addRoles(setOf(dto.studentId), toCourseRole)
        }

        val user = profileService.getUser(dto.studentId)
        val firstName = dto.fullName.split(" ")[1]
        val lastName = dto.fullName.split(" ")[0]
        val middleName = dto.fullName.split(" ").getOrNull(2)

        val updateProfileDto = user.toUpdateDto(
            email = user.email,
            firstName = firstName,
            lastName = lastName,
            middleName = middleName,
            photoId = user.photoId,
            roles = user.roles,
            userName = user.username,
        )
        profileService.updateProfile(updateProfileDto)

        return mapStudentToDto(updatedStudent)
    }

    @Transactional
    fun setCompanyToStudent(dto: CreateCompanyToStudentDto){
        studentsRepository.updateCompanyAndPosition(dto.companyId, dto.userId, dto.positionId)
    }

    @Transactional
    fun deaneryDeleteStudent(userId: UserId) {
        studentsRepository.getStudent(userId)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Студент не найден")

        studentsRepository.deleteStudent(userId)
        profileService.deleteUser(userId)
    }

    private fun mapStudentToDto(entity: StudentEntity): StudentDto {
        val (profile, companies, position) = runBlocking {
            val profileDeferred = async {
                profileService.getShortAccount(GetProfileDto(userId = entity.userId))
            }
            val companyDeferred = async {
                val ids = mutableListOf<UUID>()
                entity.companyId?.let { ids.add(it) }
                entity.logs.mapNotNull { it.companyIds }.run { ids.addAll(this.flatten()) }

                companyService.getShortCompanies(ids).groupBy { it.companyId }
            }
            val positionDeferred = async {
                try {
                    entity.positionId?.let { positionService.getPositionById(it) }
                } catch (ignore: Exception) { null }
            }
            Triple(profileDeferred.await(), companyDeferred.await(), positionDeferred.await())
        }

        return StudentDto(
            id = entity.userId,
            profile = profile,
            group = entity.group,
            course = entity.course,
            company = entity.companyId?.let { companies[it]?.firstOrNull() },
            logs = entity.logs.map { log ->
                ShortLogInfo(
                    id = log.id,
                    status = ApprovalStatus.valueOf(log.status),
                    type = LogType.valueOf(log.type),
                    createdAt = log.createdAt,
                    companies = log.companyIds?.let { companyIds ->
                        companyIds.mapNotNull { companyId ->
                            companies[companyId]?.firstOrNull()
                        }
                    } ?: emptyList(),
                )
            }.toSet(),
            position = position,
            isOnAcademicLeave = entity.isOnAcademicLeave,
        )
    }

    private fun getRoleByCourse(course: Int): Set<UserRole> {
        return when (course) {
            2 -> setOf(UserRole.STUDENT_SECOND)
            3 -> setOf(UserRole.STUDENT_THIRD)
            4 -> setOf(UserRole.STUDENT_FOURTH)
            5 -> setOf(UserRole.STUDENT_GRADUATE)
            else -> emptySet()
        }
    }

    private fun createInviteLinkDto(inviteLinkEntity: InviteLinkEntity): InviteLinkDto {
        val link = UriComponentsBuilder.fromUriString(clientUri)
            .pathSegment("registration")
            .queryParam("id", inviteLinkEntity.id)
            .queryParams(
                CollectionUtils.toMultiValueMap(
                    inviteLinkEntity.config
                        .toMap()
                        .mapValues { listOf(OBJECT_MAPPER.writeValueAsString(it.value)) }
                )
            )
            .build()
            .toUriString()

        return InviteLinkDto(
            link = link
        )
    }

    companion object {
        private val OBJECT_MAPPER = jacksonObjectMapper()
    }
}