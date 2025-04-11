package com.quqee.backend.internship_hits.public_interface.statistic

data class FullCompanyStatisticsDto (
    val title: String,
    val data: List<PositionStatisticDto>
)