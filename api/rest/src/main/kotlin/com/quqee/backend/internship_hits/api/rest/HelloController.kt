package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.api.rest.HelloApiDelegate
import com.quqee.backend.internship_hits.model.rest.HelloGet200Response
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class HelloController : HelloApiDelegate {
    override fun helloGet(): ResponseEntity<HelloGet200Response> {
        return ResponseEntity.ok(HelloGet200Response(status = "OK"))
    }
}