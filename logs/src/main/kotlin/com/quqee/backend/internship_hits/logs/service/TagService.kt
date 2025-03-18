package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.logs.entity.TagEntity
import com.quqee.backend.internship_hits.logs.repository.jpa.TagJpaRepository
import org.springframework.stereotype.Service
import java.util.*

interface TagService {
    fun createTag(name: String, companyId: UUID): TagEntity
    fun getAllTags(): List<TagEntity>
    fun updateTag(tagId: UUID, newName: String): TagEntity
}

@Service
class TagServiceImpl(
    private val tagJpaRepository: TagJpaRepository
) : TagService {

    /**
     * Создание нового тега
     */
    override fun createTag(name: String, companyId: UUID): TagEntity {
        val newTag = TagEntity(id = UUID.randomUUID(), name = name, companyId = companyId)
        return tagJpaRepository.save(newTag)
    }

    /**
     * Получение списка всех тегов
     */
    override fun getAllTags(): List<TagEntity> {
        return tagJpaRepository.findAll()
    }

    /**
     * Обновление существующего тега
     */
    override fun updateTag(tagId: UUID, newName: String): TagEntity {
        val tag = tagJpaRepository.findById(tagId).orElseThrow { NoSuchElementException("Тег с ID $tagId не найден") }
        val updatedTag = tag.copy(name = newName)
        return tagJpaRepository.save(updatedTag)
    }
}