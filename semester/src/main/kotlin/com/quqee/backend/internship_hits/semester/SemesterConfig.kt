package com.quqee.backend.internship_hits.semester

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.quqee.backend.internship_hits.semester"])
@EnableJpaRepositories(basePackages = ["com.quqee.backend.internship_hits.semester.repository"])
@EntityScan(basePackages = ["com.quqee.backend.internship_hits.semester.entity"])
open class SemesterConfig