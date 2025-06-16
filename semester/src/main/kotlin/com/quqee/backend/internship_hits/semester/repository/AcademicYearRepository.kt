package com.quqee.backend.internship_hits.semester.repository

import com.quqee.backend.internship_hits.semester.entity.AcademicYearEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AcademicYearRepository : JpaRepository<AcademicYearEntity, UUID> {
    fun findByStartYear(startYear: Int): AcademicYearEntity?
}