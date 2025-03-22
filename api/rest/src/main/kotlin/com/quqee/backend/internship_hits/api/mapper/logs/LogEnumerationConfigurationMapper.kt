package com.quqee.backend.internship_hits.api.mapper.logs

import com.quqee.backend.internship_hits.mapper.EnumerationMapper
import com.quqee.backend.internship_hits.mapper.makeEnumerationMapper
import com.quqee.backend.internship_hits.model.rest.LogTypeEnum
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LogEnumerationConfigurationMapper {
    @Bean
    fun mapLogType(): EnumerationMapper<LogTypeEnum, LogType> {
        return makeEnumerationMapper(LogTypeEnum::class, LogType::class)
    }
}