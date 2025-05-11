package com.quqee.backend.internship_hits.public_interface.check_list

data class CheckData(
    val position: Int,
    val ruName: String,
    val enName: String,
    val ruDescription: String?,
    val enDescription: String?,
    val isChecked: Boolean
)
