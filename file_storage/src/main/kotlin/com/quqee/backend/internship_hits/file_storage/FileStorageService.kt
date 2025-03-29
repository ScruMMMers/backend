package com.quqee.backend.internship_hits.file_storage

import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.file_storage_public.GetLinkForFileDto
import com.quqee.backend.internship_hits.public_interface.file_storage_public.LinkForFileDto
import com.quqee.backend.internship_hits.public_interface.file_storage_public.UploadFileDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.HeadObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.time.Duration

@Service
class FileStorageService(
    @Value("\${amazonProperties.bucketName}") private val bucketName: String,
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
) {
    fun getFileLink(dto: GetLinkForFileDto): LinkForFileDto {
        try {
            val headObjectRequest = HeadObjectRequest.builder()
                .bucket(bucketName)
                .key(dto.fileKey)
                .build()
            s3Client.headObject(headObjectRequest)
        } catch (e: Exception) {
            throw ExceptionInApplication(ExceptionType.FATAL)
        }

        val objectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(dto.fileKey)
            .build()

        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofDays(7))
            .getObjectRequest(objectRequest)
            .build()

        val presignedRequest = s3Presigner.presignGetObject(presignRequest)
        return LinkForFileDto(presignedRequest.url().toString())
    }

    fun uploadFile(dto: UploadFileDto) {
        val file = dto.file
        val metadata = dto.metadata
        try {
            val putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(metadata.fileKey)
                .metadata(metadata.getMapMetadata())
                .build()
            s3Client.putObject(
                putObjectRequest,
                RequestBody.fromInputStream(file.inputStream, file.size)
            )
        } catch (e: java.lang.Exception) {
            throw ExceptionInApplication(ExceptionType.FATAL, "Ошибка при загрузке файлов")
        }
    }

    fun deleteFile(fileKey: String) {
        try {
            s3Client.deleteObject{
                it.bucket(bucketName)
                it.key(fileKey)
            }
        } catch (e: Exception) {
            throw ExceptionInApplication(ExceptionType.FATAL, "Ошибка при удалении файла")
        }
    }
}