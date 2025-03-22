package com.quqee.backend.internship_hits.public_interface.file_storage_public

import org.springframework.web.multipart.MultipartFile

data class UploadFileDto(
    val file: MultipartFile,
    val metadata: FileMetadata,
)
