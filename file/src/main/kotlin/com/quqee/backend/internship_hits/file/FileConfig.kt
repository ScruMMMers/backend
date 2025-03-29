package com.quqee.backend.internship_hits.file

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.quqee.backend.internship_hits.file.repository.jpa"])
@EntityScan(basePackages = ["com.quqee.backend.internship_hits.file.entity"])
open class FileConfig