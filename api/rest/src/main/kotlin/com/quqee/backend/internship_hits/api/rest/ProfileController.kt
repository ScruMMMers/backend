package com.quqee.backend.internship_hits.api.rest

import com.quqee.backend.internship_hits.model.rest.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.net.URI
import java.util.*

@Component
class ProfileController : ProfileApiDelegate {
    override fun profileHeaderGet(): ResponseEntity<GetProfileHeaderResponseView> {
        return super.profileHeaderGet()
    }

    override fun profileMeGet(): ResponseEntity<GetMyProfileResponseView> {
        return ResponseEntity.ok(
            GetMyProfileResponseView(
                profile = ProfileView(
                    fullName = "Строго",
                    avatarUrl = URI.create("https://cs-config.ru/_ld/35/72750554.png"),
                    roles = listOf(
                        RoleEnum.STUDENT_SECOND
                    ),
                    course = "1 course",
                    group = "1 group",
                    shortCompany = ShortCompanyView(
                        companyId = UUID.randomUUID(),
                        name = "Яндекс",
                        avatarUrl = URI.create("https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Yandex_icon.svg/2048px-Yandex_icon.svg.png"),
                        primaryColor = "#ef1818",
                    )
                )
            )
        )
    }
}