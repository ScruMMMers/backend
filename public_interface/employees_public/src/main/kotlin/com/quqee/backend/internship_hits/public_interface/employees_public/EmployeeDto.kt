package com.quqee.backend.internship_hits.public_interface.employees_public

import com.quqee.backend.internship_hits.public_interface.common.IHaveId
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.common.UserId

data class EmployeeDto(
    override val id: UserId,
    val profile: ShortAccountDto,
    val companies: List<ShortCompanyDto>,
) : IHaveId<UserId>
