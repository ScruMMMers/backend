package com.quqee.backend.internship_hits.logs.service

import com.quqee.backend.internship_hits.logs.repository.LogsRepository
import com.quqee.backend.internship_hits.oauth2_security.KeycloakUtils
import com.quqee.backend.internship_hits.profile.ProfileService
import com.quqee.backend.internship_hits.public_interface.check_list.CheckData
import com.quqee.backend.internship_hits.public_interface.common.enums.ExceptionType
import com.quqee.backend.internship_hits.public_interface.common.exception.ExceptionInApplication
import com.quqee.backend.internship_hits.public_interface.enums.LogType
import org.springframework.stereotype.Service

interface CheckListService {
    fun getCheckList(): List<CheckData>
}

@Service
class CheckListServiceImpl(
    private val logsRepository: LogsRepository,
    private val profileService: ProfileService,
) : CheckListService {
    override fun getCheckList(): List<CheckData> {
        val userId = KeycloakUtils.getUserId()
            ?: throw ExceptionInApplication(ExceptionType.FORBIDDEN, "User is null")

        return listOf(
            CheckData(
                position = 1,
                ruName = "Написать планы по стажировке",
                enName = "Write internship plans",
                ruDescription = "Отправьте лог по планам",
                enDescription = "Send log on plans",
                isChecked = logsRepository.existsLogByUserIdAndType(userId, LogType.DEFAULT)
            ),
            CheckData(
                position = 2,
                ruName = "Написать прошедшие собеседования",
                enName = "Write past interviews",
                ruDescription = null,
                enDescription = null,
                isChecked = logsRepository.existsLogByUserIdAndType(userId, LogType.INTERVIEW)
            ),
            CheckData(
                position = 3,
                ruName = "Написать полученный оффер",
                enName = "Write the received offer",
                ruDescription = null,
                enDescription = null,
                isChecked = logsRepository.existsLogByUserIdAndType(userId, LogType.OFFER)
            ),
            CheckData(
                position = 4,
                ruName = "Написать финальный выбор",
                enName = "Write the final choice",
                ruDescription = null,
                enDescription = null,
                isChecked = logsRepository.existsLogByUserIdAndType(userId, LogType.FINAL)
            ),
            CheckData(
                position = 5,
                ruName = "Получить подтверждение финального выбора",
                enName = "Receive confirmation of final selection",
                ruDescription = null,
                enDescription = null,
                isChecked = logsRepository.existsLogByUserIdAndTypeAndApproved(userId, LogType.FINAL)
            )
        )
    }
}