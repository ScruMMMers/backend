package com.quqee.backend.internship_hits.api.mapper.statistic

import com.quqee.backend.internship_hits.mapper.*
import com.quqee.backend.internship_hits.model.rest.FullCompanyStatisticView
import com.quqee.backend.internship_hits.model.rest.PositionEnum
import com.quqee.backend.internship_hits.model.rest.PositionStatisticView
import com.quqee.backend.internship_hits.model.rest.PositionView
import com.quqee.backend.internship_hits.public_interface.common.PositionDto
import com.quqee.backend.internship_hits.public_interface.common.enums.Position
import com.quqee.backend.internship_hits.public_interface.statistic.FullCompanyStatisticsDto
import com.quqee.backend.internship_hits.public_interface.statistic.PositionStatisticDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FullCompanyStatisticConfigurationMapper(
    private val mapPositionStatistic: FromInternalToApiMapper<PositionStatisticView, PositionStatisticDto>
) {

    @Bean
    fun mapFullCompanyStatistic(): FromInternalToApiMapper<FullCompanyStatisticView, FullCompanyStatisticsDto> =
        makeToApiMapper { model ->
            FullCompanyStatisticView(
                title = model.title,
                data = model.data.map { mapPositionStatistic.fromInternal(it) }
            )
        }
}
