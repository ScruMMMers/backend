package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.FullCompanyStatisticListView
import com.quqee.backend.internship_hits.public_interface.statistic.FullCompanyStatisticsDto
import com.quqee.backend.internship_hits.statistic.service.StatisticService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class StatisticController(
    private val statisticService: StatisticService,
    private val mapFullCompanyStatisticList: FromInternalToApiMapper<FullCompanyStatisticListView, List<FullCompanyStatisticsDto>>,
) : StatisticApiDelegate {

    override fun statisticCompanyCompanyIdGet(companyId: UUID): ResponseEntity<FullCompanyStatisticListView> {
        return ResponseEntity.ok(
            mapFullCompanyStatisticList.fromInternal(
                statisticService.getCompanyStatistics(
                    companyId
                )
            )
        )
    }

}