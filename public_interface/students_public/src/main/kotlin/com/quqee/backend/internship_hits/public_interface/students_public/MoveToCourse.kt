package com.quqee.backend.internship_hits.public_interface.students_public

import com.quqee.backend.internship_hits.public_interface.common.UserId

enum class MoveToCourseType {
    BY_COURSE,
    BY_USER,
    ALL,
}

interface MoveToCourseDto {
    val type: MoveToCourseType
}

data class MoveToCourseAllDto(
    override val type: MoveToCourseType = MoveToCourseType.ALL,
) : MoveToCourseDto

data class MoveToCourseByCourseDto(
    override val type: MoveToCourseType = MoveToCourseType.BY_COURSE,
    val fromCourse: Int,
    val toCourse: Int,
) : MoveToCourseDto

data class MoveToCourseByUserDto(
    override val type: MoveToCourseType = MoveToCourseType.BY_USER,
    val userIds: Set<UserId>,
    val toCourse: Int,
) : MoveToCourseDto
