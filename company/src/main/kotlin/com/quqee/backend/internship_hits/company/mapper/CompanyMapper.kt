package com.quqee.backend.internship_hits.company.mapper

import com.quqee.backend.internship_hits.company.entity.CompanyEntity
import com.quqee.backend.internship_hits.file.service.FileService
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ColorEnum
import com.quqee.backend.internship_hits.public_interface.company.CompanyDto
import com.quqee.backend.internship_hits.public_interface.profile_public.GetProfileDto
import org.springframework.stereotype.Component
import java.net.URI

@Component
class CompanyMapper(
    private val companyPositionMapper: CompanyPositionMapper,
    private val profileService: ProfileService,
    private val fileService: FileService,
) {
    /**
     * Преобразование сущности компании в DTO представление
     */
    fun toCompanyDto(entity: CompanyEntity): CompanyDto {
        return CompanyDto(
            companyId = entity.companyId,
            name = entity.name,
            agent = profileService.getShortAccount(GetProfileDto(entity.agent)),
            avatarUrl = URI.create(fileService.getFileLink(entity.avatarId).downloadUrl),
            sinceYear = entity.sinceYear,
            description = entity.description,
            primaryColor = ColorEnum.fromHex(entity.primaryColor),
            positions = entity.positions.map { companyPositionMapper.toCompanyPositionDto(it) },
            createdAt = entity.createdAt
        )
    }

    /**
     * Преобразование сущности компании в короткое DTO представление
     */
    fun toShortCompanyDto(entity: CompanyEntity): ShortCompanyDto {
        return ShortCompanyDto(
            companyId = entity.companyId,
            name = entity.name,
            avatarUrl = URI.create(fileService.getFileLink(entity.avatarId).downloadUrl),
            primaryColor = ColorEnum.fromHex(entity.primaryColor),
            createdAt = entity.createdAt
        )
    }

} 