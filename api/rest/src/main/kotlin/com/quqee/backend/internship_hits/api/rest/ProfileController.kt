package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.model.rest.GetMyProfileResponseView
import com.quqee.backend.internship_hits.model.rest.GetProfileHeaderResponseView
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ProfileController : ProfileApiDelegate {
    override fun profileHeaderGet(): ResponseEntity<GetProfileHeaderResponseView> {
        return super.profileHeaderGet()
    }

    override fun profileMeGet(): ResponseEntity<GetMyProfileResponseView> {
        return super.profileMeGet()
    }
}