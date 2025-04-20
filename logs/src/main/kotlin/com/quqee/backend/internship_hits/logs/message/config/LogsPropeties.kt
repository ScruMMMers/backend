package com.quqee.backend.internship_hits.logs.message.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "exporter.logs")
open class LogsProperties {
    lateinit var internship: LogConfig
    lateinit var new: LogConfig

    class LogConfig {
        lateinit var sourceTopic: String
        lateinit var groupId: String
    }
}