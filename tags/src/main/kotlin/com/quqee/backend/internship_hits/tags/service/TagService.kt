package com.quqee.backend.internship_hits.tags.service

import com.quqee.backend.internship_hits.public_interface.tags.TagDto
import com.quqee.backend.internship_hits.public_interface.tags.TagListDto
import com.quqee.backend.internship_hits.tags.entity.TagEntity
import com.quqee.backend.internship_hits.tags.mapper.TagMapper
import com.quqee.backend.internship_hits.tags.repository.TagJpaRepository
import org.springframework.stereotype.Service
import java.util.*

interface TagService {
    fun createTag(name: String, companyId: UUID): TagDto
    fun updateTag(tagId: UUID, newName: String): TagDto
    fun getTagsByNamePart(name: String?): TagListDto
    fun getTagsEntityByNamePart(name: String?): List<TagEntity>
}

@Service
class TagServiceImpl(
    private val tagJpaRepository: TagJpaRepository,
    private val tagMapper: TagMapper
) : TagService {

    /**
     * Создание нового тега
     */
    override fun createTag(name: String, companyId: UUID): TagDto {
        val newTag = TagEntity(id = UUID.randomUUID(), name = name, companyId = companyId)
        val savedTag = tagJpaRepository.save(newTag)
        return tagMapper.toTagDto(savedTag)
    }

    /**
     * Обновление существующего тега
     */
    override fun updateTag(tagId: UUID, newName: String): TagDto {
        val tag = tagJpaRepository.findById(tagId).orElseThrow { NoSuchElementException("Тег с ID $tagId не найден") }
        val updatedTag = tag.copy(name = newName)
        val savedTag = tagJpaRepository.save(updatedTag)
        return tagMapper.toTagDto(savedTag)
    }

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
        return tagMapper.toTagListDto(tags)
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
}
