package com.quqee.backend.internship_hits.semester.entity

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "semesters")
data class SemesterEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id", nullable = false)
    var academicYear: AcademicYearEntity = AcademicYearEntity(),

    @Column(nullable = false)
    var name: String = "",

    var startDate: OffsetDateTime = OffsetDateTime.now(),
    var endDate: OffsetDateTime = OffsetDateTime.now(),

    var practiceDiaryStart: OffsetDateTime = OffsetDateTime.now(),
    var practiceDiaryEnd: OffsetDateTime = OffsetDateTime.now(),

    var companyChangeStart: OffsetDateTime = OffsetDateTime.now(),
    var companyChangeEnd: OffsetDateTime = OffsetDateTime.now()
)