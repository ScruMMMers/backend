package com.quqee.backend.internship_hits.logs.repository.jpa

import com.quqee.backend.internship_hits.logs.entity.CommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CommentJpaRepository : JpaRepository<CommentEntity, UUID>, JpaSpecificationExecutor<CommentEntity> {
}