package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.aggregate.UserAggregate
import ru.quipy.aggregate.UserCreatedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.UserAggregateState
import ru.quipy.logic.createUser
import java.util.*

@RestController
@RequestMapping("/user")
class UserController(
    val userEsService: EventSourcingService<UUID, UserAggregate, UserAggregateState>
) {
    @PostMapping
    fun createUser(@RequestParam userName: String, @RequestParam nickname: String): UserCreatedEvent {
        return userEsService.create{
            it.createUser(userName, nickname)
        }
    }
}