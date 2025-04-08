package com.quqee.backend.internship_hits.meeting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.quqee.backend.internship_hits.meeting", "com.quqee.backend.internship_hits.company"])
@EnableJpaRepositories(basePackages = ["com.quqee.backend.internship_hits.meeting.repository"])
@EntityScan(basePackages = ["com.quqee.backend.internship_hits.meeting.entity"])
open class MeetingConfig