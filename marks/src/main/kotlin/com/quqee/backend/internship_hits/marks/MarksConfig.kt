package com.quqee.backend.internship_hits.marks

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.quqee.backend.internship_hits.marks", "com.quqee.backend.internship_hits.students"])
@EnableJpaRepositories(basePackages = ["com.quqee.backend.internship_hits.marks.repository"])
@EntityScan(basePackages = ["com.quqee.backend.internship_hits.marks.entity"])
open class MarksConfig