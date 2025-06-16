package com.quqee.backend.internship_hits.semester.repository

import com.quqee.backend.internship_hits.semester.entity.SemesterEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SemesterRepository : JpaRepository<SemesterEntity, UUID> {

}