package com.quqee.backend.internship_hits.mapper

import kotlin.reflect.KClass

interface EnumerationMapper<ApiType : Any, InternalType : Any> {
    fun mapToApi(internal: InternalType): ApiType
    fun mapToInternal(api: ApiType): InternalType
    fun apiType(): KClass<ApiType>
    fun internalType(): KClass<InternalType>
}