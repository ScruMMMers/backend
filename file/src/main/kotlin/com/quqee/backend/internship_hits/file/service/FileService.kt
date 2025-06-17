package com.quqee.backend.internship_hits.file.service

import com.quqee.backend.internship_hits.file.entity.FileEntity
import com.quqee.backend.internship_hits.file.mapper.FileMapper
import com.quqee.backend.internship_hits.file.repository.FileRepository
import com.quqee.backend.internship_hits.file_storage.FileStorageService
import com.quqee.backend.internship_hits.public_interface.common.FileDownloadLinkDto
import com.quqee.backend.internship_hits.public_interface.common.FileDto
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.file_storage_public.FileMetadata
import com.quqee.backend.internship_hits.public_interface.file_storage_public.GetLinkForFileDto
import com.quqee.backend.internship_hits.public_interface.file_storage_public.UploadFileDto
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface FileService {
    /**
     * Загружает файл в хранилище и сохраняет метаданные в БД
     * @param file Загружаемый файл
     * @return ID созданной записи о файле
     */
    fun uploadFile(file: MultipartFile): FileDto

    fun uploadFileWithId(file: MultipartFile, fileId: UUID): FileDto
    /**
     * Получает ссылку для доступа к файлу
     * @param fileId ID файла в БД
     * @return URL для доступа к файлу
     */
    fun getFileLink(fileId: UUID): FileDownloadLinkDto
    
    /**
     * Удаляет файл из хранилища и метаданные из БД
     * @param fileId ID файла в БД
     */
    fun deleteFile(fileId: UUID)
    
    /**
     * Получает метаданные файла по ID
     * @param fileId ID файла в БД
     * @return Сущность с метаданными файла
     */
    fun getFileById(fileId: UUID): FileDto

    fun getZipByIds(fileIds: List<UUID>): ByteArray
}

@Service
class FileServiceImpl(
    private val fileRepository: FileRepository,
    private val fileMapper: FileMapper,
    private val fileStorageService: FileStorageService
) : FileService {
    
    override fun uploadFile(file: MultipartFile): FileDto {
        val fileId = UUID.randomUUID()
        return uploadFileWithId(file, fileId)
    }

    override fun uploadFileWithId(file: MultipartFile, fileId: UUID): FileDto {
        val fileName = "${file.originalFilename}"
        val fileKey = "${fileId}_${file.originalFilename}"
        val contentType = file.contentType ?: "application/octet-stream"
        val fileSize = file.size

        val fileMetadata = FileMetadata(fileKey, fileName, contentType, fileSize)
        val uploadFileDto = UploadFileDto(file, fileMetadata)
        fileStorageService.uploadFile(uploadFileDto)

        val fileEntity = FileEntity(
            id = fileId,
            fileKey = fileKey,
            fileName = fileName,
            contentType = contentType,
            fileSize = fileSize
        )

        return fileMapper.toDto(fileRepository.saveFile(fileEntity))
    }
    
    override fun getFileLink(fileId: UUID): FileDownloadLinkDto {
        val fileEntity = getFileEntityById(fileId)
        val getLinkDto = GetLinkForFileDto(fileEntity.fileKey)
        return FileDownloadLinkDto(fileStorageService.getFileLink(getLinkDto).link)
    }
    
    override fun deleteFile(fileId: UUID) {
        val fileEntity = getFileEntityById(fileId)
        fileStorageService.deleteFile(fileEntity.fileKey)
        fileRepository.deleteFile(fileEntity)
    }

    override fun getFileById(fileId: UUID): FileDto {
        val fileEntity = getFileEntityById(fileId)
        return fileMapper.toDto(fileEntity)
    }

    override fun getZipByIds(fileIds: List<UUID>): ByteArray {
        val fileEntities = fileRepository.findAllById(fileIds)

        val fileKeys = fileEntities.map { it.fileKey }

        return fileStorageService.getArchiveForFiles(fileKeys)
    }

    private fun getFileEntityById(fileId: UUID): FileEntity {
        return fileRepository.findById(fileId).orElseThrow {
            ExceptionInApplication(ExceptionType.BAD_REQUEST, "Файл с ID: $fileId не найден")
        }
    }
}