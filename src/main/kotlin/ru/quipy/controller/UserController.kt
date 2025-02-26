package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.aggregate.UserAggregate
import ru.quipy.aggregate.UserCreatedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.UserAggregateState
import ru.quipy.logic.createUser
import ru.quipy.projections.data.UserData
import ru.quipy.projections.repository.UserProjectionRepository
import java.util.*

@RestController
@RequestMapping("/user")
class UserController(
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>,
    val userProjectionRepository: UserProjectionRepository
) {
    @PostMapping
    fun createUser(@RequestParam userName: String, @RequestParam nickname: String): UserCreatedEvent {
        return userEsService.create{
            it.createUser(userName, nickname)
        }
    }

    @GetMapping("/searchUser")
    fun getUserByName(@RequestParam nickname: String): UserData? {
        val user = userProjectionRepository.findByNickname(nickname)
            ?: throw IllegalArgumentException("No user found with nickname: $nickname")
        return user
    }
}