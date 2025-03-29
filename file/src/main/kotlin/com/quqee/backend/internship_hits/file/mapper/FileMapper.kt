package com.quqee.backend.internship_hits.file.mapper

import com.quqee.backend.internship_hits.file.entity.FileEntity
import com.quqee.backend.internship_hits.file_storage.FileStorageService
import com.quqee.backend.internship_hits.public_interface.common.FileDto
import com.quqee.backend.internship_hits.public_interface.file_storage_public.GetLinkForFileDto
import org.springframework.stereotype.Component

@Component
class FileMapper(
    private val fileStorageService: FileStorageService
) {
    fun toDto(fileEntity: FileEntity): FileDto {
        return FileDto(
            id = fileEntity.id,
            fileName = fileEntity.fileName,
            fileKey = fileEntity.fileKey,
            contentType = fileEntity.contentType,
            fileSize = fileEntity.fileSize,
            downloadUrl = fileStorageService.getFileLink(GetLinkForFileDto(fileEntity.fileKey)).link
        )
    }
}