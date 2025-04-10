package com.quqee.backend.internship_hits.company

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.quqee.backend.internship_hits.company", "com.quqee.backend.internship_hits.tags", "com.quqee.backend.internship_hits.position"])
@EnableJpaRepositories(basePackages = ["com.quqee.backend.internship_hits.company.repository.jpa"])
@EntityScan(basePackages = ["com.quqee.backend.internship_hits.company.entity"])
open class CompanyConfig