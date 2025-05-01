package com.quqee.backend.internship_hits.public_interface.mark

import com.quqee.backend.internship_hits.public_interface.common.UserId

data class CreateMarkDto (
    val userId: UserId,
    val mark: Int,
    val semester: Int?
)