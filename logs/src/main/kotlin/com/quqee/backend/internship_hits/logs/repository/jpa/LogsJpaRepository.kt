package com.quqee.backend.internship_hits.logs.repository.jpa

import com.quqee.backend.internship_hits.logs.entity.LogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface LogsJpaRepository : JpaRepository<LogEntity, UUID>, JpaSpecificationExecutor<LogEntity>
