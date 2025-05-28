package com.quqee.backend.internship_hits.api.mapper.employees

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.EmployeeView
import com.quqee.backend.internship_hits.model.rest.FileView
import com.quqee.backend.internship_hits.model.rest.ShortCompanyView
import com.quqee.backend.internship_hits.public_interface.common.FileDto
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.employees_public.EmployeeDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class EmployeesConfigurationMapper(
    private val companyMapper: FromInternalToApiMapper<ShortCompanyView, ShortCompanyDto>,
    private val fileToApiMapper: FromInternalToApiMapper<FileView, FileDto>
    ) {
    @Bean
    open fun employeeViewMapper(): FromInternalToApiMapper<EmployeeView, EmployeeDto> = makeToApiMapper { model ->
        EmployeeView(
            userId = model.profile.userId,
            fullName = model.profile.fullName,
            companies = model.companies.map { companyMapper.fromInternal(it) },
            email = model.profile.email,
            photo = model.profile.avatar?.let { fileToApiMapper.fromInternal(it) },
        )
    }
}