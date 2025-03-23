package com.quqee.backend.internship_hits.company.mapper

import com.quqee.backend.internship_hits.company.entity.CompanyEntity
import com.quqee.backend.internship_hits.public_interface.common.ShortAccountDto
import com.quqee.backend.internship_hits.public_interface.common.ShortCompanyDto
import com.quqee.backend.internship_hits.public_interface.common.enums.RoleEnum
import com.quqee.backend.internship_hits.public_interface.company.CompanyDto
import org.springframework.stereotype.Component
import java.net.URI
import java.util.*

@Component
class CompanyMapper(
) {
    /**
     * Преобразование сущности компании в DTO представление
     */
    fun toCompanyView(entity: CompanyEntity): CompanyDto {
        return CompanyDto(
            companyId = entity.companyId,
            name = entity.name,
            agent = ShortAccountDto(
                userId = UUID.randomUUID().toString(),
                fullName = "Эвилоныч",
                avatarUrl = URI.create("https://sun9-25.userapi.com/s/v1/ig2/_BSpBnS-Zo2c2_J48KJk9POa1GDKa37nEJSdVoe-qeyNbIBoQmp4N4N6TtRIhr5xRvhB6O8VCBW2ke3jl9y2Y3NV.jpg?quality=96&as=32x43,48x64,72x96,108x144,160x214,240x320,360x480,480x641,540x721,640x854,720x961,959x1280&from=bu&u=o6kxoiUgTUztSx1nrI9fyiJnFMYWW64BuWCLXbMMpfc&cs=605x807")
                    .toString(),
                roles = listOf(RoleEnum.DEANERY),
                primaryColor = "#533af9"
            ),
            avatarUri = URI.create("https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Yandex_icon.svg/2048px-Yandex_icon.svg.png"), //TODO получение урла фото
            sinceYear = entity.sinceYear,
            description = entity.description,
            primaryColor = entity.primaryColor,
            positions = listOf() //TODO получение из сервиса позиций компании
        )
    }

    /**
     * Преобразование сущности компании в короткое DTO представление
     */
    fun toShortCompanyView(entity: CompanyEntity): ShortCompanyDto {
        return ShortCompanyDto(
            companyId = entity.companyId,
            name = entity.name,
            avatarUrl = URI.create("https://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Yandex_icon.svg/2048px-Yandex_icon.svg.png"), //TODO получение урла фото
            primaryColor = entity.primaryColor
        )
    }
    

} 