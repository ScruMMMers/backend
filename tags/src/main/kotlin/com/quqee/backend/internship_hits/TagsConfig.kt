package com.quqee.backend.internship_hits

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.quqee.backend.internship_hits.tags"])
@EnableJpaRepositories(basePackages = ["com.quqee.backend.internship_hits.tags.repository"])
@EntityScan(basePackages = ["com.quqee.backend.internship_hits.tags.entity"])
open class TagsConfig