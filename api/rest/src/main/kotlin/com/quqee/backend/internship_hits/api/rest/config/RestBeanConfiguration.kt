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
        "com.quqee.backend.internship_hits.company",
        "com.quqee.backend.internship_hits.tags",
        "com.quqee.backend.internship_hits.tags_query",
        "com.quqee.backend.internship_hits.file",
        "com.quqee.backend.internship_hits.position",
        "com.quqee.backend.internship_hits.meeting",
        "com.quqee.backend.internship_hits.statistic",
        "com.quqee.backend.internship_hits.announcement",
        "com.quqee.backend.internship_hits.locker",
        "com.quqee.backend.internship_hits.document",
        "com.quqee.backend.internship_hits.marks",
        "com.quqee.backend.internship_hits.semester",
    ]
)
open class RestBeanConfiguration
