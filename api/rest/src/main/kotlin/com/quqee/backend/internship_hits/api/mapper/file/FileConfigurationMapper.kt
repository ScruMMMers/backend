package com.quqee.backend.internship_hits.api.mapper.file

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.FileView
import com.quqee.backend.internship_hits.public_interface.common.FileDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FileConfigurationMapper {
    @Bean
    fun fileToApi(): FromInternalToApiMapper<FileView, FileDto> = makeToApiMapper { model ->
        FileView(
            id = model.id,
            fileName = model.fileName,
            contentType = model.contentType,
            fileSize = model.fileSize,
            fileKey = model.fileKey,
            uri = model.downloadUrl
        )
    }

    @Bean
    fun fileToInternal(): FromApiToInternalMapper<FileView, FileDto> = makeFromApiMapper { model ->
        FileDto(
            id = model.id,
            fileName = model.fileName,
            fileKey = model.fileKey,
            contentType = model.contentType,
            fileSize = model.fileSize,
            downloadUrl = model.uri
        )
    }
}