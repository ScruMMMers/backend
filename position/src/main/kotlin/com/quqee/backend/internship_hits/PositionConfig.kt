package com.quqee.backend.internship_hits

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.quqee.backend.internship_hits.position"])
@EnableJpaRepositories(basePackages = ["com.quqee.backend.internship_hits.position.repository"])
@EntityScan(basePackages = ["com.quqee.backend.internship_hits.position.entity"])
open class PositionConfig