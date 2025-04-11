package com.quqee.backend.internship_hits.api.mapper.statistic

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.PositionStatisticView
import com.quqee.backend.internship_hits.public_interface.statistic.PositionStatisticDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PositionStatisticConfigurationMapper(
) {

    @Bean
    fun mapPositionStatistic(): FromInternalToApiMapper<PositionStatisticView, PositionStatisticDto> =
        makeToApiMapper { model ->
            PositionStatisticView(
                position = model.position,
                value = model.value,
            )
        }
}
