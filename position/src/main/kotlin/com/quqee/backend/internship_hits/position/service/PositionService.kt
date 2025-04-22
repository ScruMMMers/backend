package com.quqee.backend.internship_hits.position.service

import com.quqee.backend.internship_hits.position.entity.PositionEntity
import com.quqee.backend.internship_hits.position.mapper.PositionMapper
import com.quqee.backend.internship_hits.position.repository.PositionJpaRepository
import com.quqee.backend.internship_hits.public_interface.common.PositionDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import org.springframework.stereotype.Service

interface PositionService {
    fun getPositionList(): List<PositionDto>
    fun getPositionById(id: Long): PositionDto
    fun getPositionEntityById(id: Long): PositionEntity
    fun getPositionListByPartName(name: String?): List<PositionDto>
    fun getPositionEntityByPartName(name: String?): List<PositionEntity>
}

@Service
class PositionServiceImpl(
    private val positionRepository: PositionJpaRepository,
    private val positionMapper: PositionMapper,
): PositionService {
    override fun getPositionList(): List<PositionDto> {
        return positionRepository.findAll().map { positionMapper.toDto(it) }
    }

    override fun getPositionById(id: Long): PositionDto {
        return positionMapper.toDto(getPositionEntityById(id))
    }

    override fun getPositionEntityById(id: Long): PositionEntity {
        return positionRepository.findById(id)
            .orElseThrow { ExceptionInApplication(ExceptionType.NOT_FOUND, "Вакансия с '${id}' не найдена") }
    }

    override fun getPositionListByPartName(name: String?): List<PositionDto> {
        return if (name.isNullOrBlank()) {
            getPositionList()
        } else {
            positionRepository.findByPartName(name).map { positionMapper.toDto(it) }
        }
    }

    override fun getPositionEntityByPartName(name: String?): List<PositionEntity> {
        return if (name.isNullOrBlank()) {
            positionRepository.findAll()
        } else {
            positionRepository.findByPartName(name)
        }
    }
}