package com.quqee.backend.internship_hits.marks.repository

import com.quqee.backend.internship_hits.marks.entity.MarkEntity
import com.quqee.backend.internship_hits.public_interface.common.UserId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MarkRepository : JpaRepository<MarkEntity, UUID> {

    fun findByUserIdAndSemester(userId: UserId, semester: Int): Optional<MarkEntity>

    fun findAllByUserIdOrderBySemesterDesc(userId: UserId): List<MarkEntity>

    fun findFirstByUserIdOrderBySemesterDesc(userId: UserId): Optional<MarkEntity>

}