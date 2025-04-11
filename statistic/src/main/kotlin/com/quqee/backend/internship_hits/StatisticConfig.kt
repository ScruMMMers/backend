package com.quqee.backend.internship_hits

import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(
    scanBasePackages = [
        "com.quqee.backend.internship_hits.statistic",
        "com.quqee.backend.internship_hits.company",
        "com.quqee.backend.internship_hits.logs",
        "com.quqee.backend.internship_hits.position",
    ]
)
open class StatisticConfig