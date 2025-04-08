package com.quqee.backend.internship_hits.tags_query.service

import com.quqee.backend.internship_hits.public_interface.tags.TagListDto
import com.quqee.backend.internship_hits.tags.entity.TagEntity
import com.quqee.backend.internship_hits.tags.repository.TagJpaRepository
import com.quqee.backend.internship_hits.tags_query.mapper.TagQueryMapper
import org.springframework.stereotype.Service

interface TagQueryService {
    fun getTagsByNamePart(name: String?): TagListDto
    fun getTagsEntityByNamePart(name: String?): List<TagEntity>
    fun mapTagEntityToDto(tagEntityList: List<TagEntity>): TagListDto
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
        val tags = if (name.isNullOrBlank()) {
            tagJpaRepository.findAll()
        } else {
            tagJpaRepository.findByNameContainingIgnoreCase(name)
        }
        return tags
    }

    /**
     * Маппинг сущностей тегов в список тегов
     */
    override fun mapTagEntityToDto(tagEntityList: List<TagEntity>): TagListDto {
        return tagQueryMapper.toTagListDto(tagEntityList)
    }
}
