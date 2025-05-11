package com.quqee.backend.internship_hits.file.repository

import com.quqee.backend.internship_hits.file.entity.FileEntity
import com.quqee.backend.internship_hits.file.repository.jpa.FileJpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class FileRepository(private val fileJpaRepository: FileJpaRepository) {
    
    fun saveFile(fileEntity: FileEntity): FileEntity {
        return fileJpaRepository.save(fileEntity)
    }
    
    fun deleteFile(fileEntity: FileEntity) {
        fileJpaRepository.delete(fileEntity)
    }
    
    fun findById(fileId: UUID): java.util.Optional<FileEntity> {
        return fileJpaRepository.findById(fileId)
    }

    fun findAllById(fileIds: List<UUID>): List<FileEntity> {
        return fileJpaRepository.findAllById(fileIds)
    }
} 