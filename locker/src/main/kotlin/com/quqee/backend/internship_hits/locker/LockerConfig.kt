package com.quqee.backend.internship_hits.locker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.quqee.backend.internship_hits.locker"])
@EnableJpaRepositories(basePackages = [
    "com.quqee.backend.internship_hits.locker.repository"
])
@EntityScan(basePackages = [
    "com.quqee.backend.internship_hits.locker.entity",
])
open class LockerConfig