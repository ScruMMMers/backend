package com.quqee.backend.internship_hits.api.rest.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.quqee.backend.internship_hits.profile",
        "com.quqee.backend.internship_hits.notification",
        "com.quqee.backend.internship_hits.logs",
        "com.quqee.backend.internship_hits.api",
    ]
)
open class RestBeanConfiguration
