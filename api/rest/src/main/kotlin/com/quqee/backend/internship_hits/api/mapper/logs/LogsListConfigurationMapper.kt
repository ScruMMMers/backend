package com.quqee.backend.internship_hits.api.mapper.logs

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.LastIdPaginationView
import com.quqee.backend.internship_hits.model.rest.LogView
import com.quqee.backend.internship_hits.model.rest.LogsListView
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import com.quqee.backend.internship_hits.public_interface.logs.LogDto
import com.quqee.backend.internship_hits.public_interface.logs.LogListDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class LogsListConfigurationMapper(
    private val mapLog: FromInternalToApiMapper<LogView, LogDto>,
    private val mapPagination: FromInternalToApiMapper<LastIdPaginationView, LastIdPagination>
) {
    @Bean
    open fun mapLogsList(): FromInternalToApiMapper<LogsListView, LogListDto> = makeToApiMapper { model ->
        LogsListView(
            logs = model.logs.map { mapLog.fromInternal(it) },
            page = mapPagination.fromInternal(model.page)
        )
    }
}
