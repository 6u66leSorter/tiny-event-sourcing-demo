package ru.quipy.projections.service

import org.springframework.stereotype.Service
import ru.quipy.aggregate.*
import ru.quipy.projections.data.UserData
import ru.quipy.projections.repository.UserProjectionRepository

@Service
class UserService(
    private val userProjectionRepository: UserProjectionRepository
) {
    fun handle(event: UserCreatedEvent) {
        val projectionData = UserData(
            userId = event.userId.toString(),
            nickname = event.nickname,
            userName = event.userName
        )
        userProjectionRepository.save(projectionData)
    }
}