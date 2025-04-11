package com.quqee.backend.internship_hits.api.mapper.statistic

import com.quqee.backend.internship_hits.mapper.*
import com.quqee.backend.internship_hits.model.rest.*
import com.quqee.backend.internship_hits.public_interface.common.PositionDto
import com.quqee.backend.internship_hits.public_interface.common.enums.Position
import com.quqee.backend.internship_hits.public_interface.statistic.FullCompanyStatisticsDto
import com.quqee.backend.internship_hits.public_interface.statistic.PositionStatisticDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FullCompanyStatisticListConfigurationMapper(
    private val mapFullCompanyStatistic: FromInternalToApiMapper<FullCompanyStatisticView, FullCompanyStatisticsDto>
) {

    @Bean
    fun mapFullCompanyStatisticList(): FromInternalToApiMapper<FullCompanyStatisticListView, List<FullCompanyStatisticsDto>> =
        makeToApiMapper { model ->
            FullCompanyStatisticListView(
                statistic = model.map { mapFullCompanyStatistic.fromInternal(it) }
            )
        }
}
