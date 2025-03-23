package com.quqee.backend.internship_hits.api.mapper.common

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.LastIdPaginationView
import com.quqee.backend.internship_hits.public_interface.common.IHaveId
import com.quqee.backend.internship_hits.public_interface.common.LastIdPagination
import com.quqee.backend.internship_hits.public_interface.common.LastIdPaginationResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.UUID

@Configuration
class LastIdPaginationConfigurationMapper {
    @Bean
    fun mapLastIdPagination(): FromApiToInternalMapper<LastIdPaginationView, LastIdPagination> = makeFromApiMapper { model ->
        LastIdPagination(
            lastId = model.lastId,
            pageSize = model.pageSize,
            hasNext = model.hasNext
        )
    }

    @Bean
    fun mapLastIdPaginationToApi(): FromInternalToApiMapper<LastIdPaginationView, LastIdPagination> = makeToApiMapper { model ->
        LastIdPaginationView(
            pageSize = model.pageSize,
            hasNext = model.hasNext,
            lastId = model.lastId
        )
    }

    @Bean
    fun lastIdPaginationResponseMapper(): FromInternalToApiMapper<LastIdPaginationView, LastIdPaginationResponse<*, UUID>> = makeToApiMapper { model ->
        LastIdPaginationView(
            pageSize = model.actualPageSize,
            hasNext = model.hasNextPage,
            lastId = model.lastId
        )
    }
}
