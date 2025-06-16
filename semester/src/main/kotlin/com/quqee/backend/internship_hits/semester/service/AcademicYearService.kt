package com.quqee.backend.internship_hits.semester.service

import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.semester_interface.AcademicYearDto
import com.quqee.backend.internship_hits.public_interface.semester_interface.SemesterDto
import com.quqee.backend.internship_hits.public_interface.semester_interface.UpdateSemesterDto
import com.quqee.backend.internship_hits.semester.entity.AcademicYearEntity
import com.quqee.backend.internship_hits.semester.entity.SemesterEntity
import com.quqee.backend.internship_hits.semester.repository.AcademicYearRepository
import com.quqee.backend.internship_hits.semester.repository.SemesterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Service
open class AcademicYearService(
    private val academicYearRepository: AcademicYearRepository,
    private val semesterRepository: SemesterRepository
) {

    @Transactional
    open fun createAcademicYear(startYear: Int): AcademicYearDto {
        if (academicYearRepository.findByStartYear(startYear) != null) {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Учебный год уже существует")
        }

        val academicYear = AcademicYearEntity(
            id = UUID.randomUUID(),
            startYear = startYear,
            endYear = startYear + 1
        )
        val savedYear = academicYearRepository.save(academicYear)

        val zoneOffset = ZoneOffset.UTC

        val firstSemester = SemesterEntity(
            id = UUID.randomUUID(),
            academicYear = savedYear,
            name = "Первый семестр",
            startDate = OffsetDateTime.of(startYear, 9, 1, 0, 0, 0, 0, zoneOffset),
            endDate = OffsetDateTime.of(startYear + 1, 2, 1, 0, 0, 0, 0, zoneOffset),
            practiceDiaryStart = OffsetDateTime.of(startYear, 9, 1, 0, 0, 0, 0, zoneOffset),
            practiceDiaryEnd = OffsetDateTime.of(startYear + 1, 2, 1, 0, 0, 0, 0, zoneOffset),
            companyChangeStart = OffsetDateTime.of(startYear, 9, 1, 0, 0, 0, 0, zoneOffset),
            companyChangeEnd = OffsetDateTime.of(startYear + 1, 2, 1, 0, 0, 0, 0, zoneOffset)
        )

        val secondSemester = SemesterEntity(
            id = UUID.randomUUID(),
            academicYear = savedYear,
            name = "Второй семестр",
            startDate = OffsetDateTime.of(startYear + 1, 2, 2, 0, 0, 0, 0, zoneOffset),
            endDate = OffsetDateTime.of(startYear + 1, 7, 1, 0, 0, 0, 0, zoneOffset),
            practiceDiaryStart = OffsetDateTime.of(startYear + 1, 2, 2, 0, 0, 0, 0, zoneOffset),
            practiceDiaryEnd = OffsetDateTime.of(startYear + 1, 7, 1, 0, 0, 0, 0, zoneOffset),
            companyChangeStart = OffsetDateTime.of(startYear + 1, 2, 2, 0, 0, 0, 0, zoneOffset),
            companyChangeEnd = OffsetDateTime.of(startYear + 1, 7, 1, 0, 0, 0, 0, zoneOffset)
        )

        savedYear.semesters.addAll(listOf(firstSemester, secondSemester))
        val updatedYear = academicYearRepository.save(savedYear)

        return mapToDto(updatedYear)
    }

    fun getAllAcademicYears(): List<AcademicYearDto> =
        academicYearRepository.findAll().map { mapToDto(it) }

    fun getSemestersByYear(yearId: UUID): List<SemesterDto> {
        val year = academicYearRepository.findById(yearId)
            .orElseThrow { ExceptionInApplication(ExceptionType.BAD_REQUEST, "Учебный год не найден") }
        return year.semesters.map { mapSemesterToDto(it) }
    }

    fun updateSemester(semesterId: UUID, updated: UpdateSemesterDto): SemesterDto {
        val existing = semesterRepository.findById(semesterId)
            .orElseThrow {
                ExceptionInApplication(ExceptionType.BAD_REQUEST, "Семестр не найден")
            }

        val updatedSemester = existing.copy(
            startDate = updated.startDate,
            endDate = updated.endDate,
            practiceDiaryStart = updated.practiceDiaryStart,
            practiceDiaryEnd = updated.practiceDiaryEnd,
            companyChangeStart = updated.companyChangeStart,
            companyChangeEnd = updated.companyChangeEnd
        )

        return mapSemesterToDto(semesterRepository.save(updatedSemester))
    }

    private fun mapToDto(entity: AcademicYearEntity): AcademicYearDto =
        AcademicYearDto(
            id = entity.id,
            yearRange = "${entity.startYear}/${entity.endYear}",
            semesters = entity.semesters.map { mapSemesterToDto(it) }
        )

    private fun mapSemesterToDto(entity: SemesterEntity): SemesterDto =
        SemesterDto(
            id = entity.id,
            name = entity.name,
            startDate = entity.startDate,
            endDate = entity.endDate,
            practiceDiaryStart = entity.practiceDiaryStart,
            practiceDiaryEnd = entity.practiceDiaryEnd,
            companyChangeStart = entity.companyChangeStart,
            companyChangeEnd = entity.companyChangeEnd
        )
}