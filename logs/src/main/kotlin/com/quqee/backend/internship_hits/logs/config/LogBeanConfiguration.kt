package com.quqee.backend.internship_hits.logs.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.quqee.backend.internship_hits.tags",
        "com.quqee.backend.internship_hits.profile",
        "com.quqee.backend.internship_hits.file",
    ]
)
open class LogBeanConfiguration