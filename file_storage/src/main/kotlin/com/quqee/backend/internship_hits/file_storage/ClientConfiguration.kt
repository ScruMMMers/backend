package com.quqee.backend.internship_hits.file_storage

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.net.URI

@Configuration
open class ClientConfiguration(
    @Value("\${amazonProperties.endpointUrl}") private val endpointUrl: String,
) {
    @Bean
    open fun s3Client(): S3Client {
        return S3Client.builder()
            .endpointOverride(URI.create(endpointUrl))
            .build()
    }

    @Bean
    open fun s3Presigner(): S3Presigner {
        return S3Presigner.builder()
            .endpointOverride(URI.create(endpointUrl))
            .build()
    }
}