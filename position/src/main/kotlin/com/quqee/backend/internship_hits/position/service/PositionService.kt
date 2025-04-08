package com.quqee.backend.internship_hits.position.service

import com.quqee.backend.internship_hits.position.entity.PositionEntity
import com.quqee.backend.internship_hits.position.repository.PositionJpaRepository
import com.quqee.backend.internship_hits.public_interface.common.PositionDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import org.springframework.stereotype.Service

interface PositionService {
    fun getPositionList(): List<PositionDto>
    fun getPositionById(id: Long): PositionDto
    fun getPositionListByPartName(name: String?): List<PositionDto>
}

@Service
class PositionServiceImpl(
    private val positionRepository: PositionJpaRepository
): PositionService {
    override fun getPositionList(): List<PositionDto> {
        return positionRepository.findAll().map { it.toDto() }
    }

    override fun getPositionById(id: Long): PositionDto {
        return positionRepository.findById(id)
            .orElseThrow { ExceptionInApplication(ExceptionType.NOT_FOUND, "Вакансия с '${id}' не найдена") }
            .toDto()
    }

    override fun getPositionListByPartName(name: String?): List<PositionDto> {
        return if (name.isNullOrBlank()) {
            getPositionList()
        } else {
            positionRepository.findByPartName(name).map { it.toDto() }
        }
    }

    private fun PositionEntity.toDto(): PositionDto {
        return PositionDto(
            id = id,
            name = name,
            position = position
        )
    }
}