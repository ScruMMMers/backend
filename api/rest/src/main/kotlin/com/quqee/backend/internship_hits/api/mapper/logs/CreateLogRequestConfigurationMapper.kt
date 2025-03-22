package com.quqee.backend.internship_hits.api.mapper.logs

import com.quqee.backend.internship_hits.mapper.*
import com.quqee.backend.internship_hits.model.rest.CreateLogRequestView
import com.quqee.backend.internship_hits.model.rest.LogTypeEnum
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.logs.CreateLogRequestDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class CreateLogRequestConfigurationMapper(
    private val mapLogType: EnumerationMapper<LogTypeEnum, LogType>
) {
    @Bean
    open fun mapCreateLogRequest(): FromApiToInternalMapper<CreateLogRequestView, CreateLogRequestDto> = makeFromApiMapper { model ->
        CreateLogRequestDto(
            message = model.message,
            type = mapLogType.mapToInternal(model.type)
        )
    }

    @Bean
    open fun mapCreateLogRequestToApi(): FromInternalToApiMapper<CreateLogRequestView, CreateLogRequestDto> = makeToApiMapper { model ->
        CreateLogRequestView(
            message = model.message,
            type = mapLogType.mapToApi(model.type)
        )
    }
}
