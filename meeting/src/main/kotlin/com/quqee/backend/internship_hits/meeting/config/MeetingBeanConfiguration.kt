package com.quqee.backend.internship_hits.meeting.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.quqee.backend.internship_hits.company",
    ]
)
open class MeetingBeanConfiguration