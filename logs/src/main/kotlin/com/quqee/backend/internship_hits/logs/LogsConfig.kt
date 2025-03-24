package com.quqee.backend.internship_hits.logs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.quqee.backend.internship_hits.logs"])
@EnableJpaRepositories(basePackages = ["com.quqee.backend.internship_hits.logs.repository.jpa"])
@EntityScan(basePackages = ["com.quqee.backend.internship_hits.logs.entity"])
open class LogsConfig