package com.quqee.backend.internship_hits.public_interface.common

interface CompanyStatisticsProjection {
    fun getLogType(): String
    fun getPositionId(): Long
    fun getLogCount(): Long
}