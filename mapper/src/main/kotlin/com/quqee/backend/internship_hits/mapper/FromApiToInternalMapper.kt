package com.quqee.backend.internship_hits.mapper

import kotlin.reflect.KClass

interface FromApiToInternalMapper<ApiType : Any, InternalType : Any> {
    fun fromApi(item: ApiType): InternalType

    fun apiType(): KClass<ApiType>

    fun internalType(): KClass<InternalType>
}