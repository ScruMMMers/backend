package com.quqee.backend.internship_hits.public_interface.common

import kotlin.reflect.full.memberProperties

fun <K, V> Map<K, V?>.filterNullValues(): Map<K, V> {
    return this.filterValues { it != null }
        .mapValues { it.value as V }
}

interface IActionParams {
    fun toMap(): Map<String, Any> {
        return this::class.memberProperties.associate { property ->
            property.name to property.getter.call(this)
        }.filterNullValues()
    }
}