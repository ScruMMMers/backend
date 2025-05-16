package com.quqee.backend.internship_hits.api.mapper.students

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.MoveStudentsViewByCourse
import com.quqee.backend.internship_hits.model.rest.MoveStudentsViewByUser
import com.quqee.backend.internship_hits.model.rest.ShortCompanyView
import com.quqee.backend.internship_hits.model.rest.StudentShortLogView
import com.quqee.backend.internship_hits.model.rest.StudentView
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.logs.ShortLogInfo
import com.quqee.backend.internship_hits.public_interface.students_public.MoveToCourseByCourseDto
import com.quqee.backend.internship_hits.public_interface.students_public.MoveToCourseByUserDto
import com.quqee.backend.internship_hits.public_interface.students_public.MoveToCourseDto
import com.quqee.backend.internship_hits.public_interface.students_public.StudentDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class StudentsConfigurationMapper(
    private val companyMapper: FromInternalToApiMapper<ShortCompanyView, ShortCompanyDto>,
    private val mapStudentShortLogToApi: FromInternalToApiMapper<StudentShortLogView, ShortLogInfo>,
) {
    @Bean
    open fun studentViewMapper(): FromInternalToApiMapper<StudentView, StudentDto> = makeToApiMapper { model ->
        StudentView(
            userId = model.profile.userId,
            group = model.group,
            course = model.course,
            fullName = model.profile.fullName,
            company = model.company?.let { companyMapper.fromInternal(it) },
            logs = model.logs
                .map { mapStudentShortLogToApi.fromInternal(it) }
                .groupBy { it.type.name },
            avatarUrl = model.profile.avatarUrl,
        )
    }

    @Bean
    open fun moveByCourseMapper(): FromApiToInternalMapper<MoveStudentsViewByCourse, MoveToCourseDto> =
        makeFromApiMapper { view ->
            MoveToCourseByCourseDto(
                fromCourse = view.fromCourse,
                toCourse = view.toCourse,
            )
        }

    @Bean
    open fun moveByUserMapper(): FromApiToInternalMapper<MoveStudentsViewByUser, MoveToCourseDto> =
        makeFromApiMapper { view ->
            MoveToCourseByUserDto(
                userIds = view.userIds.toSet(),
                toCourse = view.toCourse,
            )
        }
}