package com.quqee.backend.internship_hits.internship.service

import com.quqee.backend.internship_hits.internship.entity.InternshipEntity
import java.util.UUID

interface InternshipService {
    fun changeInternship(companyId: UUID, positionId: Long)
    fun getInternshipByUser(userId: UUID): InternshipEntity
    fun getMyIntership(): InternshipEntity
}