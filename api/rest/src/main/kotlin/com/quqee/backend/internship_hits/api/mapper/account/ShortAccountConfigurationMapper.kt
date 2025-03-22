package com.quqee.backend.internship_hits.api.mapper.account

import com.quqee.backend.internship_hits.mapper.*
import com.quqee.backend.internship_hits.model.rest.RoleEnum
import com.quqee.backend.internship_hits.model.rest.ShortAccountView
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.common.enums.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.net.URI
import java.util.*

@Configuration
class ShortAccountConfigurationMapper(
    private val mapRole: EnumerationMapper<RoleEnum, Role>
) {
    @Bean
    fun mapShortAccount(): FromApiToInternalMapper<ShortAccountView, ShortAccountDto> = makeFromApiMapper { model ->
        ShortAccountDto(
            userId = model.userId.toString(),
            fullName = model.fullName,
            roles = model.roles.map { mapRole.mapToInternal(it) },
            avatarUrl = model.avatarUrl.toString(),
            primaryColor = model.primaryColor
        )
    }

    @Bean
    fun mapShortAccountToApi(): FromInternalToApiMapper<ShortAccountView, ShortAccountDto> = makeToApiMapper { model ->
        ShortAccountView(
            userId = UUID.fromString(model.userId),
            fullName = model.fullName,
            roles = model.roles.map { mapRole.mapToApi(it) },
            avatarUrl = URI(model.avatarUrl),
            primaryColor = model.primaryColor
        )
    }
}

