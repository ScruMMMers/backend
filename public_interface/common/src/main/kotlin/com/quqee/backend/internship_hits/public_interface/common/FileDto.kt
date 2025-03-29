package com.quqee.backend.internship_hits.public_interface.common

import java.util.*

data class FileDto(
    val id: UUID,
    val fileName: String,
    val fileKey: String,
    val contentType: String,
    val fileSize: Long,
    val downloadUrl: String
)
