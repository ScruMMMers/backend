package com.quqee.backend.internship_hits.file.repository.jpa

import com.quqee.backend.internship_hits.file.entity.FileEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FileJpaRepository : JpaRepository<FileEntity, UUID> {}