package com.quqee.backend.internship_hits.tags_query.service

import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.tags.TagListDto
import com.quqee.backend.internship_hits.tags.entity.TagEntity
import com.quqee.backend.internship_hits.tags.repository.TagJpaRepository
import com.quqee.backend.internship_hits.tags_query.mapper.TagQueryMapper
import org.apache.commons.text.similarity.LevenshteinDistance
import org.springframework.stereotype.Service
import java.util.*

interface TagQueryService {
    fun getTagsByNamePart(name: String?): TagListDto
    fun getTagsEntityByNamePart(name: String?): List<TagEntity>
    fun mapTagEntityToDto(tagEntityList: List<TagEntity>): TagListDto
    fun getTagByCompanyId(companyId: UUID): TagEntity
}

@Service
class TagQueryServiceImpl(
    private val tagJpaRepository: TagJpaRepository,
    private val tagQueryMapper: TagQueryMapper
) : TagQueryService {
    /**
     * Получение списка тегов, где имя содержит часть строки
     * Если строка пустая, возвращаем все теги
     */
    override fun getTagsByNamePart(name: String?): TagListDto {
        val tags = if (name.isNullOrBlank()) {
            tagJpaRepository.findAll()
        } else {
            tagJpaRepository.findByNameContainingIgnoreCase(name)
        }
        return mapTagEntityToDto(tags)
    }

    /**
     * Получение списка сущностей тегов, где имя содержит часть строки
     * Если строка пустая, возвращаем все теги
     */
    override fun getTagsEntityByNamePart(name: String?): List<TagEntity> {
        val allTags = tagJpaRepository.findAll()

        if (name.isNullOrBlank()){
            return allTags
        }

        val basicResults = tagJpaRepository.findByNameContainingIgnoreCase(name)
        if (basicResults.isNotEmpty()) {
            return basicResults
        }

        val distance = LevenshteinDistance.getDefaultInstance()
        val threshold = 3

        return allTags.filter { tag ->
            distance.apply(name.lowercase(), tag.name.lowercase()) <= threshold
        }
    }

    /**
     * Маппинг сущностей тегов в список тегов
     */
    override fun mapTagEntityToDto(tagEntityList: List<TagEntity>): TagListDto {
        return tagQueryMapper.toTagListDto(tagEntityList)
    }

    /**
     * Получение тега по ID компании
     */
    override fun getTagByCompanyId(companyId: UUID): TagEntity {
        val tag = tagJpaRepository.findByCompanyId(companyId)
            ?: throw ExceptionInApplication(ExceptionType.NOT_FOUND, "Компания с ID $companyId не найдена")
        return tag
    }
}
