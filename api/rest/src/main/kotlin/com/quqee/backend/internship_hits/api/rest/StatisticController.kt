package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.model.rest.FullCompanyStatisticListView
import com.quqee.backend.internship_hits.model.rest.FullCompanyStatisticView
import com.quqee.backend.internship_hits.model.rest.PositionStatisticView
import com.quqee.backend.internship_hits.public_interface.statistic.FullCompanyStatisticsDto
import com.quqee.backend.internship_hits.statistic.service.StatisticService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class StatisticController(
    private val statisticService: StatisticService,
    private val mapFullCompanyStatisticList: FromInternalToApiMapper<FullCompanyStatisticListView, List<FullCompanyStatisticsDto>>,
) : StatisticApiDelegate {

    override fun statisticCompanyCompanyIdGet(companyId: UUID): ResponseEntity<FullCompanyStatisticListView> {
//        return ResponseEntity.ok(
//            mapFullCompanyStatisticList.fromInternal(
//                statisticService.getCompanyStatistics(
//                    companyId
//                )
//            )
//        )
        return ResponseEntity.ok(
            FullCompanyStatisticListView(
                listOf(
                    FullCompanyStatisticView(
                        "Статистика по проведенным интервью",
                        listOf(
                            PositionStatisticView(
                                "BACKEND",
                                12
                            ),
                            PositionStatisticView(
                                "FRONTEND",
                                15
                            ),
                            PositionStatisticView(
                                "ML",
                                5
                            ),
                            PositionStatisticView(
                                "ONE_S",
                                1
                            )
                        )
                    ),
                    FullCompanyStatisticView(
                        "Статистика по полученным офферам",
                        listOf(
                            PositionStatisticView(
                                "BACKEND",
                                10
                            ),
                            PositionStatisticView(
                                "FRONTEND",
                                8
                            ),
                            PositionStatisticView(
                                "ML",
                                2
                            ),
                            PositionStatisticView(
                                "ONE_S",
                                1
                            )
                        )
                    ),
                    FullCompanyStatisticView(
                        "Статистика по финальному выбору студентов",
                        listOf(
                            PositionStatisticView(
                                "BACKEND",
                                4
                            ),
                            PositionStatisticView(
                                "FRONTEND",
                                3
                            ),
                            PositionStatisticView(
                                "ML",
                                2
                            ),
                            PositionStatisticView(
                                "ONE_S",
                                0
                            )
                        )
                    ),
                )
            )
        )
    }

}