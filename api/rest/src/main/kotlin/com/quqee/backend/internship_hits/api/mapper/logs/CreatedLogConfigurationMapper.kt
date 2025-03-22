package com.quqee.backend.internship_hits.api.mapper.logs

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.CreatedLogView
import com.quqee.backend.internship_hits.model.rest.LogView
import com.quqee.backend.internship_hits.public_interface.logs.CreatedLogDto
import com.quqee.backend.internship_hits.public_interface.logs.LogDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CreatedLogConfigurationMapper(
    private val mapLog: FromApiToInternalMapper<LogView, LogDto>,
    private val mapLogApi: FromInternalToApiMapper<LogView, LogDto>
) {
    @Bean
    open fun mapCreatedLog(): FromApiToInternalMapper<CreatedLogView, CreatedLogDto> = makeFromApiMapper { model ->
        CreatedLogDto(
            log = mapLog.fromApi(model.log)
        )
    }

    @Bean
    open fun mapCreatedLogToApi(): FromInternalToApiMapper<CreatedLogView, CreatedLogDto> = makeToApiMapper { model ->
        CreatedLogView(
            log = mapLogApi.fromInternal(model.log)
        )
    }
}
