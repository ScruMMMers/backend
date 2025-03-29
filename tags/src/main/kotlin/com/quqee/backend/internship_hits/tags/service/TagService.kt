package com.quqee.backend.internship_hits.tags.service

import com.quqee.backend.internship_hits.tags.entity.TagEntity
import com.quqee.backend.internship_hits.tags.repository.TagJpaRepository
import org.springframework.stereotype.Service
import java.util.*

interface TagService {
    fun createTag(name: String, companyId: UUID): UUID
    fun updateTag(tagId: UUID, newName: String): UUID
}

@Service
class TagServiceImpl(
    private val tagJpaRepository: TagJpaRepository
) : TagService {

    /**
     * Создание нового тега
     */
    override fun createTag(name: String, companyId: UUID): UUID {
        val newTag = TagEntity(id = UUID.randomUUID(), name = name, companyId = companyId)
        val savedTag = tagJpaRepository.save(newTag)
        return return savedTag.id
    }

    /**
     * Обновление существующего тега
     */
    override fun updateTag(tagId: UUID, newName: String): UUID {
        val tag = tagJpaRepository.findById(tagId).orElseThrow { NoSuchElementException("Тег с ID $tagId не найден") }
        val updatedTag = tag.copy(name = newName)
        val savedTag = tagJpaRepository.save(updatedTag)
        return savedTag.id
    }
}
