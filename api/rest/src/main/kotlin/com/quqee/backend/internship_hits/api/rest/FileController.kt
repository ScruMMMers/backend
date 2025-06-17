package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.file.service.FileService
import com.quqee.backend.internship_hits.file.service.PracticeDiaryTemplateService
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.FileDownloadLinkResponseView
import com.quqee.backend.internship_hits.model.rest.FileView
import com.quqee.backend.internship_hits.public_interface.common.FileDto
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.util.*

@Component
class FileController(
    private val fileService: FileService,
    private val practiceDiaryTemplateService: PracticeDiaryTemplateService,
    private val mapFile: FromInternalToApiMapper<FileView, FileDto>,
) : FileApiDelegate {
    override fun filesFileIdDeleteDelete(fileId: UUID): ResponseEntity<Unit> {
        return ResponseEntity.ok(fileService.deleteFile(fileId))
    }

    override fun filesFileIdGet(fileId: UUID): ResponseEntity<FileView> {
        return ResponseEntity.ok(mapFile.fromInternal(fileService.getFileById(fileId)))
    }

    override fun filesFileIdDownloadLinkGet(fileId: UUID): ResponseEntity<FileDownloadLinkResponseView> {
        val result = fileService.getFileLink(fileId)
        return ResponseEntity.ok(FileDownloadLinkResponseView(URI(result.downloadUrl)))
    }

    override fun filesPost(file: MultipartFile?): ResponseEntity<FileView> {
        if (file != null) {
            val result = fileService.uploadFile(file)
            return ResponseEntity.ok(mapFile.fromInternal(result))
        } else {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Invalid file path")
        }
    }

    override fun filesPracticeDiaryDeleteDelete(): ResponseEntity<Unit> {
        return ResponseEntity.ok(practiceDiaryTemplateService.deletePracticeDiary())
    }

    override fun filesPracticeDiaryGet(): ResponseEntity<FileView> {
        return ResponseEntity.ok(mapFile.fromInternal(practiceDiaryTemplateService.getPracticeDiary()))
    }

    override fun filesPracticeDiaryUploadPost(file: MultipartFile?): ResponseEntity<FileView> {
        if (file != null) {
            val result = practiceDiaryTemplateService.uploadPracticeDiary(file)
            return ResponseEntity.ok(mapFile.fromInternal(result))
        } else {
            throw ExceptionInApplication(ExceptionType.BAD_REQUEST, "Invalid file path")
        }
    }
}