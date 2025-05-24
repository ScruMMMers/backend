package com.quqee.backend.internship_hits.public_interface.students_public

import com.quqee.backend.internship_hits.public_interface.common.IHaveId
import com.quqee.backend.internship_hits.public_interface.common.PositionDto
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.common.UserId
import com.quqee.backend.internship_hits.public_interface.logs.ShortLogInfo

data class StudentDto(
    override val id: UserId,
    val profile: ShortAccountDto,
    val group: String,
    val course: Int,
    val logs: Set<ShortLogInfo>,
    val position: PositionDto?,
    val company: ShortCompanyDto?,
) : IHaveId<UserId>
