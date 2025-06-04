package com.quqee.backend.internship_hits.company.service

import com.quqee.backend.internship_hits.company.entity.CompanyPositionEntity
import com.quqee.backend.internship_hits.company.mapper.CompanyPositionMapper
import com.quqee.backend.internship_hits.company.repository.jpa.CompanyJpaRepository
import com.quqee.backend.internship_hits.company.repository.jpa.CompanyPositionJpaRepository
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.company_position.CompanyPositionDto
import com.quqee.backend.internship_hits.public_interface.company_position.CreateCompanyPositionDto
import org.springframework.stereotype.Service
import java.util.*

interface CompanyPositionService {
    fun createCompanyPosition(companyId: UUID, createCompanyPositionDto: CreateCompanyPositionDto): CompanyPositionDto
    fun incrementInterviewsCount(companyId: UUID, positionId: Long)
    fun decreaseInterviewsCount(companyId: UUID, positionId: Long)
    fun incrementEmployedCount(companyId: UUID, positionId: Long)
    fun decreaseEmployedCount(companyId: UUID, positionId: Long)
}

@Service
class CompanyPositionServiceImpl(
    private val companyPositionJpaRepository: CompanyPositionJpaRepository,
    private val companyJpaRepository: CompanyJpaRepository,
    private val mapper: CompanyPositionMapper
) : CompanyPositionService {
    override fun createCompanyPosition(
        companyId: UUID,
        createCompanyPositionDto: CreateCompanyPositionDto
    ): CompanyPositionDto {
        val companyEntity = companyJpaRepository.findById(companyId)
            .orElseThrow {
                ExceptionInApplication(
                    ExceptionType.NOT_FOUND,
                    "Компания не найдена с идентификатором $createCompanyPositionDto.companyId"
                )
            }

        if (companyPositionJpaRepository.findFirstByCompanyAndPositionId(
                companyEntity,
                createCompanyPositionDto.positionId
            ).isPresent
        ) {
            throw ExceptionInApplication(
                ExceptionType.BAD_REQUEST,
                "В компании уже есть позиция с таким стеком"
            )
        }

        val companyPositionEntity = CompanyPositionEntity(
            id = UUID.randomUUID(),
            company = companyEntity,
            positionId = createCompanyPositionDto.positionId,
            interviewsCount = 0,
            employedCount = 0,
        )

        return mapper.toCompanyPositionDto(companyPositionJpaRepository.save(companyPositionEntity))
    }

    override fun incrementInterviewsCount(companyId: UUID, positionId: Long) {
        val entity = companyPositionJpaRepository.findByCompanyAndPositionId(companyId, positionId).orElseThrow {
            ExceptionInApplication(
                ExceptionType.NOT_FOUND,
                "Позиция не найдена в компании с идентификатором $companyId"
            )
        }
        val updatedEntity = entity.copy(interviewsCount = entity.interviewsCount + 1)
        companyPositionJpaRepository.save(updatedEntity)
    }

    override fun decreaseInterviewsCount(companyId: UUID, positionId: Long) {
        val entity = companyPositionJpaRepository.findByCompanyAndPositionId(companyId, positionId).orElseThrow {
            ExceptionInApplication(
                ExceptionType.NOT_FOUND,
                "Позиция не найдена в компании с идентификатором $companyId"
            )
        }
        val updatedEntity = entity.copy(interviewsCount = entity.interviewsCount - 1)
        companyPositionJpaRepository.save(updatedEntity)
    }

    override fun incrementEmployedCount(companyId: UUID, positionId: Long) {
        val entity = companyPositionJpaRepository.findByCompanyAndPositionId(companyId, positionId).orElseThrow {
            ExceptionInApplication(
                ExceptionType.NOT_FOUND,
                "Позиция не найдена в компании с идентификатором $companyId"
            )
        }
        val updatedEntity = entity.copy(employedCount = entity.employedCount + 1)
        companyPositionJpaRepository.save(updatedEntity)
    }

    override fun decreaseEmployedCount(companyId: UUID, positionId: Long) {
        val entity = companyPositionJpaRepository.findByCompanyAndPositionId(companyId, positionId).orElseThrow {
            ExceptionInApplication(
                ExceptionType.NOT_FOUND,
                "Позиция не найдена в компании с идентификатором $companyId"
            )
        }
        val updatedEntity = entity.copy(employedCount = entity.employedCount - 1)
        companyPositionJpaRepository.save(updatedEntity)
    }


}