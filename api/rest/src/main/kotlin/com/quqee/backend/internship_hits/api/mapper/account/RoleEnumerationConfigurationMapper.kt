package com.quqee.backend.internship_hits.api.mapper.account

import com.quqee.backend.internship_hits.mapper.EnumerationMapper
import com.quqee.backend.internship_hits.mapper.makeEnumerationMapper
import com.quqee.backend.internship_hits.model.rest.RoleEnum
import com.quqee.backend.internship_hits.public_interface.common.enums.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RoleEnumerationConfigurationMapper {
    @Bean
    fun mapRoleType(): EnumerationMapper<RoleEnum, Role> {
        return makeEnumerationMapper(RoleEnum::class, Role::class)
    }
}