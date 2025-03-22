package com.quqee.backend.internship_hits.core.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.quqee.backend.internship_hits.api.rest",
        "com.quqee.backend.internship_hits.oauth2_security",
        "com.quqee.backend.internship_hits.profile",
        "com.quqee.backend.internship_hits.notification",
    ]
)
open class BeanConfiguration
