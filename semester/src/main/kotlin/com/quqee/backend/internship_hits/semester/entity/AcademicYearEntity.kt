package com.quqee.backend.internship_hits.semester.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "academic_years")
data class AcademicYearEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    var startYear: Int = 0,

    @Column(nullable = false)
    var endYear: Int = startYear + 1,

    @OneToMany(mappedBy = "academicYear", cascade = [CascadeType.ALL], orphanRemoval = true)
    var semesters: MutableList<SemesterEntity> = ArrayList()
)