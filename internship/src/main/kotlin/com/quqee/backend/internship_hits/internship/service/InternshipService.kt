package com.quqee.backend.internship_hits.internship.service

import com.quqee.backend.internship_hits.internship.entity.InternshipEntity
import com.quqee.backend.internship_hits.internship.mapper.InternshipMapper
import com.quqee.backend.internship_hits.internship.repository.InternshipJpaRepository
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.internship_interface.InternshipDto
import jakarta.transaction.Transactional
import java.time.OffsetDateTime
import java.util.UUID

interface InternshipService {
    fun createInternship(companyId: UUID, positionId: Long): InternshipDto
    fun changeInternship(companyId: UUID, positionId: Long): InternshipDto
    fun getInternshipByUser(userId: UUID): InternshipDto
    fun getInternshipsByCompany(companyId: UUID): List<InternshipDto>
    fun getMyInternship(): InternshipDto
}

open class InternshipServiceImpl(
    private val internshipJpaRepository: InternshipJpaRepository,
    private val internshipMapper: InternshipMapper
) : InternshipService {

    @Transactional
    override fun createInternship(companyId: UUID, positionId: Long): InternshipDto {
        val currentUserId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "User is null")

        val existing = internshipJpaRepository.findByUserId(currentUserId)
        if (existing != null) {
            throw ExceptionInApplication(ExceptionType.CONFLICT, "У пользователя уже есть стажировка")
        }

        val newInternship = InternshipEntity(
            id = UUID.randomUUID(),
            userId = currentUserId,
            companyId = companyId,
            positionId = positionId,
            startedAt = OffsetDateTime.now()
        )

        internshipJpaRepository.save(newInternship)

        return internshipMapper.toDto(currentUserId, newInternship)
    }

    @Transactional
    override fun changeInternship(companyId: UUID, positionId: Long): InternshipDto {
        val currentUserId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "User is null")

        val internship = internshipJpaRepository.findByUserId(currentUserId)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Стажировка не найдена для текущего пользователя ")

        val newInternship = internship.copy(
            companyId = companyId,
            positionId = positionId,
            startedAt = OffsetDateTime.now()
        )
        internshipJpaRepository.save(newInternship)

        return internshipMapper.toDto(newInternship.userId, newInternship)
    }

    override fun getInternshipByUser(userId: UUID): InternshipDto {
        val internship = internshipJpaRepository.findByUserId(userId)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Стажировка не найдена для пользователя $userId")

        return internshipMapper.toDto(userId, internship)
    }

    override fun getInternshipsByCompany(companyId: UUID): List<InternshipDto> {
        val internships = internshipJpaRepository.findAllByCompanyId(companyId)
        return internships.map { internshipMapper.toDto(it.userId, it) }
    }

    override fun getMyInternship(): InternshipDto {
        val currentUserId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "User is null")

        return getInternshipByUser(currentUserId)
    }
}
