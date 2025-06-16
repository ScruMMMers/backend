package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.AcademicYearView
import com.quqee.backend.internship_hits.model.rest.CreateAcademicYearView
import com.quqee.backend.internship_hits.model.rest.SemesterView
import com.quqee.backend.internship_hits.model.rest.UpdateSemesterView
import com.quqee.backend.internship_hits.public_interface.semester_interface.AcademicYearDto
import com.quqee.backend.internship_hits.public_interface.semester_interface.SemesterDto
import com.quqee.backend.internship_hits.public_interface.semester_interface.UpdateSemesterDto
import com.quqee.backend.internship_hits.semester.service.AcademicYearService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class SemesterController(
    private val academicYearService: AcademicYearService,
    private val mapYearToApi: FromInternalToApiMapper<AcademicYearView, AcademicYearDto>,
    private val mapSemesterToApi: FromInternalToApiMapper<SemesterView, SemesterDto>,
    private val mapUpdateSemesterToInternal: FromApiToInternalMapper<UpdateSemesterView, UpdateSemesterDto>
): YearApiDelegate {
    override fun academicYearAllGet(): ResponseEntity<List<AcademicYearView>> {
        val years = academicYearService.getAllAcademicYears()
        return ResponseEntity.ok(years.map { year -> mapYearToApi.fromInternal(year) })
    }

    override fun academicYearCreatePost(createAcademicYearView: CreateAcademicYearView): ResponseEntity<AcademicYearView> {
        val year = academicYearService.createAcademicYear(createAcademicYearView.startYear)
        return ResponseEntity.ok(mapYearToApi.fromInternal(year))
    }

    override fun academicYearSemesterSemesterIdPut(
        semesterId: UUID,
        updateSemesterView: UpdateSemesterView
    ): ResponseEntity<SemesterView> {
        val updateSemester = academicYearService.updateSemester(semesterId, mapUpdateSemesterToInternal.fromApi(updateSemesterView))
        return ResponseEntity.ok(mapSemesterToApi.fromInternal(updateSemester))
    }
}