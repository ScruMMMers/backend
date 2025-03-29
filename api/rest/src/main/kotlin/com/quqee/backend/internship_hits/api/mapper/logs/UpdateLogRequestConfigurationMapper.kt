package com.quqee.backend.internship_hits.api.mapper.logs

import com.quqee.backend.internship_hits.mapper.*
import com.quqee.backend.internship_hits.model.rest.LogTypeEnum
import com.quqee.backend.internship_hits.model.rest.UpdateLogRequestView
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.logs.UpdateLogRequestDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class UpdateLogRequestConfigurationMapper(
    private val mapLogType: EnumerationMapper<LogTypeEnum, LogType>
) {
    @Bean
    open fun mapUpdateLogRequest(): FromApiToInternalMapper<UpdateLogRequestView, UpdateLogRequestDto> = makeFromApiMapper { model ->
        UpdateLogRequestDto(
            message = model.message,
            type = mapLogType.mapToInternal(model.type),
            files = model.files
        )
    }

    @Bean
    open fun mapUpdateLogRequestToApi(): FromInternalToApiMapper<UpdateLogRequestView, UpdateLogRequestDto> = makeToApiMapper { model ->
        UpdateLogRequestView(
            message = model.message,
            type = mapLogType.mapToApi(model.type),
            files = model.files
        )
    }
}
