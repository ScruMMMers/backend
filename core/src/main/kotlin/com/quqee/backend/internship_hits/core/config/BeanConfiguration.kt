package com.quqee.backend.internship_hits.core.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
@ComponentScan(
    basePackages = [
        "com.quqee.backend.internship_hits.api.rest",
        "com.quqee.backend.internship_hits.api.websocket",
        "com.quqee.backend.internship_hits.oauth2_security",
        "com.quqee.backend.internship_hits.profile",
        "com.quqee.backend.internship_hits.notification",
        "com.quqee.backend.internship_hits.logs",
        "com.quqee.backend.internship_hits.tags",
        "com.quqee.backend.internship_hits.company",
        "com.quqee.backend.internship_hits.cdc",
        "com.quqee.backend.internship_hits.websocket_common",
        "com.quqee.backend.internship_hits.students",
        "com.quqee.backend.internship_hits.employees",
    ]
)
open class BeanConfiguration {
    @Bean
    open fun clock(): Clock = Clock.systemDefaultZone()
}
