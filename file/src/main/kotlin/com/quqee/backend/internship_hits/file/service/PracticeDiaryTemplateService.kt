package com.quqee.backend.internship_hits.file.service

import com.quqee.backend.internship_hits.file.repository.FileRepository
import com.quqee.backend.internship_hits.public_interface.common.FileDto
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class PracticeDiaryTemplateService(
    private val fileService: FileService,
    private val fileRepository: FileRepository,
) {
    private val practiceDiaryTemplateId = UUID.fromString("20163f9e-5729-4c13-8f33-7345634a9a4b")

    fun uploadPracticeDiary(file: MultipartFile): FileDto {
        val fileOpt = fileRepository.findById(practiceDiaryTemplateId)
        if (fileOpt.isPresent) {
            deletePracticeDiary()
        }

        return fileService.uploadFileWithId(file, practiceDiaryTemplateId)
    }

    fun getPracticeDiary(): FileDto {
        return fileService.getFileById(practiceDiaryTemplateId)
    }

    fun deletePracticeDiary() {
        fileService.deleteFile(practiceDiaryTemplateId)
    }
}