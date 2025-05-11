package com.quqee.backend.internship_hits.logs.repository.jpa

import com.quqee.backend.internship_hits.logs.entity.LogEntity
import com.quqee.backend.internship_hits.public_interface.common.CompanyStatisticsProjection
import com.quqee.backend.internship_hits.public_interface.enums.ApprovalStatus
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface LogsJpaRepository : JpaRepository<LogEntity, UUID>, JpaSpecificationExecutor<LogEntity> {

    @Query(nativeQuery = true, value = """
        SELECT
            p.id AS positionId,
            l.type AS logType,
            COUNT(l.id) AS logCount
        FROM
            logs l
                JOIN
            log_positions lp ON l.id = lp.log_id
                JOIN
            positions p ON lp.position_id = p.id
                JOIN
            log_tags lt ON l.id = lt.log_id
                JOIN
            tags t ON lt.tag_id = t.id
        WHERE
            t.company_id = :companyId
          AND (
            l.type != 'FINAL'
                OR (l.type = 'FINAL' AND l.approval_status = 'APPROVED')
            )
        GROUP BY
            p.id, l.type
        ORDER BY
            p.id, l.type
    """)
    fun getStatisticByCompany(@Param("companyId") companyId: UUID): List<CompanyStatisticsProjection>

    fun existsByUserIdAndType(userId: UUID, type: LogType): Boolean

    fun existsByUserIdAndTypeAndApprovalStatus(userId: UUID, type: LogType, approvalStatus: ApprovalStatus): Boolean
}
