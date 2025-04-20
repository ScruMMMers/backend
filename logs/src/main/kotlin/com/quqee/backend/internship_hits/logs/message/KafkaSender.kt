package com.quqee.backend.internship_hits.logs.message

interface KafkaSender<T> {
    fun send(message: T)
}