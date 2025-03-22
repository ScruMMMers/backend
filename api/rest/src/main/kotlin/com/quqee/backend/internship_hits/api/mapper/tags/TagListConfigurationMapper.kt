package com.quqee.backend.internship_hits.api.mapper.tags

import com.quqee.backend.internship_hits.mapper.FromApiToInternalMapper
import com.quqee.backend.internship_hits.mapper.FromInternalToApiMapper
import com.quqee.backend.internship_hits.mapper.makeFromApiMapper
import com.quqee.backend.internship_hits.mapper.makeToApiMapper
import com.quqee.backend.internship_hits.model.rest.TagView
import com.quqee.backend.internship_hits.model.rest.TagsListView
import com.quqee.backend.internship_hits.public_interface.tags.TagDto
import com.quqee.backend.internship_hits.public_interface.tags.TagListDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TagListConfigurationMapper{
    @Bean
    fun mapTagsListDtoTo(
        mapTagViewToDto: FromInternalToApiMapper<TagView, TagDto>
    ): FromInternalToApiMapper<TagsListView, TagListDto> = makeToApiMapper { model ->
        TagsListView(
            tags = model.tags.map { mapTagViewToDto.fromInternal(it) }
        )
    }

    @Bean
    fun mapTagsListViewToDto(
        mapTagDtoToView: FromApiToInternalMapper<TagView, TagDto>
    ): FromApiToInternalMapper<TagsListView, TagListDto> = makeFromApiMapper { model ->
        TagListDto(
            tags = model.tags.map { mapTagDtoToView.fromApi(it) }
        )
    }
}
