package com.quqee.backend.internship_hits.api.mapper.logs

import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.CheckDataView
import com.quqee.backend.internship_hits.public_interface.check_list.CheckData
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CheckDataConfigurationMapper {
    @Bean
    open fun mapCheckDataToApi(): FromInternalToApiMapper<CheckDataView, CheckData> = makeToApiMapper { model ->
        CheckDataView(
            position = model.position,
            ruName = model.ruName,
            enName = model.enName,
            ruDescription = model.ruDescription,
            enDescription = model.enDescription,
            isChecked = model.isChecked,
        )
    }
}