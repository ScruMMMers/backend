package com.quqee.backend.internship_hits.core.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.quqee.backend.internship_hits.api.rest",
    ]
)
open class BeanConfiguration
