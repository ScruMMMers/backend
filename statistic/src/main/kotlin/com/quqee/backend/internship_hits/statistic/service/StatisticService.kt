package com.quqee.backend.internship_hits.statistic.service

import com.quqee.backend.internship_hits.logs.service.LogsService
import com.quqee.backend.internship_hits.position.service.PositionService
import com.quqee.backend.internship_hits.public_interface.common.CompanyStatisticsProjection
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import com.quqee.backend.internship_hits.public_interface.statistic.FullCompanyStatisticsDto
import com.quqee.backend.internship_hits.public_interface.statistic.PositionStatisticDto
import org.springframework.stereotype.Service
import java.util.*

interface StatisticService {
    fun getCompanyStatistics(companyId: UUID): List<FullCompanyStatisticsDto>
}

@Service
class StatisticServiceImpl(
    private val logsService: LogsService,
    private val positionService: PositionService
) : StatisticService {

    private val logTypesForStatistic = setOf(
        LogType.INTERVIEW,
        LogType.OFFER,
        LogType.FINAL
    )

    override fun getCompanyStatistics(companyId: UUID): List<FullCompanyStatisticsDto> {
        val statistic = logsService.getLogsForStatistic(companyId)
        return transformStatistics(statistic)
    }

    private fun transformStatistics(stats: List<CompanyStatisticsProjection>): List<FullCompanyStatisticsDto> {
        val groupedStats = stats
            .filter { projection ->
                logTypesForStatistic.any { it.name == projection.getLogType() }
            }
            .groupBy { it.getLogType() }

        val mainStatistics = groupedStats.map { (logType, group) ->
            val logTypeEnum = LogType.valueOf(logType)
            FullCompanyStatisticsDto(
                title = logTypeEnum.statisticName,
                data = group.map {
                    val position = positionService.getPositionById(it.getPositionId())
                    PositionStatisticDto(
                        position = "${position.name} ${position.position}",
                        value = it.getLogCount()
                    )
                }
            )
        }

        return mainStatistics + listOfNotNull(
            createRatioStatistic(
                "Соотношение офферов к интервью",
                LogType.OFFER,
                LogType.INTERVIEW,
                groupedStats
            ),
            createRatioStatistic(
                "Соотношение финальных решений к офферам",
                LogType.FINAL,
                LogType.OFFER,
                groupedStats
            )
        )
    }

    private fun createRatioStatistic(
        title: String,
        numeratorType: LogType,
        denominatorType: LogType,
        groupedStats: Map<String, List<CompanyStatisticsProjection>>
    ): FullCompanyStatisticsDto? {
        val numeratorStats = groupedStats[numeratorType.name] ?: return null
        val denominatorStats = groupedStats[denominatorType.name] ?: return null

        val numeratorByPosition = numeratorStats.groupBy { it.getPositionId() }
        val denominatorByPosition = denominatorStats.groupBy { it.getPositionId() }

        val allPositionIds = (numeratorByPosition.keys + denominatorByPosition.keys).toSet()

        val ratioData = allPositionIds.mapNotNull { positionId ->
            val numeratorCount = numeratorByPosition[positionId]?.sumOf { it.getLogCount() } ?: 0L
            val denominatorCount = denominatorByPosition[positionId]?.sumOf { it.getLogCount() } ?: 0L

            val ratioValue: Long = if (denominatorCount != 0L) {
                (numeratorCount.toDouble() / denominatorCount.toDouble() * 100).toLong()
            } else {
                0
            }

            val position = positionService.getPositionById(positionId)
            PositionStatisticDto(
                position = "${position.name} ${position.position}",
                value = ratioValue
            )
        }

        return FullCompanyStatisticsDto(
            title = title,
            data = ratioData
        )
    }
}
