package com.quqee.backend.internship_hits.file.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "files")
data class FileEntity(
    @Id
    var id: UUID,

    @Column(name = "file_key", nullable = false, unique = true)
    var fileKey: String,

    @Column(name = "file_name", nullable = false)
    var fileName: String,

    @Column(name = "content_type", nullable = false)
    var contentType: String,

    @Column(name = "file_size", nullable = false)
    var fileSize: Long,
    
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        UUID.randomUUID(),
        "",
        "",
        "",
        0,
        LocalDateTime.now(),
        LocalDateTime.now()
    )

    override fun toString(): String {
        return "FileEntity(id=$id, fileKey='$fileKey', fileName='$fileName', contentType='$contentType', fileSize=$fileSize, createdAt=$createdAt, updatedAt=$updatedAt)"
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
    
    @PreUpdate
    fun onUpdate() {
        updatedAt = LocalDateTime.now()
    }
}