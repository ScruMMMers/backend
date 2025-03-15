package com.quqee.backend.internship_hits.public_interface.common.exception

class ExceptionInApplication(
    val type: ExceptionType
) : RuntimeException()