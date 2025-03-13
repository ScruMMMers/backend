package com.quqee.backend.internship_hits.mapper

import kotlin.reflect.KClass

interface FromInternalToApiMapper<ApiType : Any, InternalType : Any> {
    fun fromInternal(item: InternalType): ApiType

    fun apiType(): KClass<ApiType>

    fun internalType(): KClass<InternalType>
}