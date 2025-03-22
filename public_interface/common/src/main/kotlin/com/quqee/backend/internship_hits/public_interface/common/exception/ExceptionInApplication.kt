package com.quqee.backend.internship_hits.public_interface.common.exception

class ExceptionInApplication(
    val type: ExceptionType,
    override val message: String = "Exception in application",
) : RuntimeException(message)