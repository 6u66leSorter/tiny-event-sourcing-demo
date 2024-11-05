package ru.quipy.logic

import ru.quipy.aggregate.UserCreatedEvent
import java.util.*

fun UserAggregateState.createUser(userName: String, nickname: String): UserCreatedEvent {
    return UserCreatedEvent(UUID.randomUUID(), userName, nickname)
}