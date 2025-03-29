package com.quqee.backend.internship_hits.public_interface.file_storage_public

import java.util.HashMap

data class FileMetadata(
    val fileKey: String,
    val fileName: String,
    val contentType: String,
    val contentLength: Long,
) {
    fun getMapMetadata(): Map<String, String> {
        val map = HashMap<String, String>(2)
        map[MetadataConst.CONTENT_TYPE.type] = contentType
        map[MetadataConst.CONTENT_LENGTH.type] = contentLength.toString()
        return map
    }
}
