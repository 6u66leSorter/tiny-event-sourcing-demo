package ru.quipy.logic

import ru.quipy.aggregate.TaskAddedEvent
import ru.quipy.aggregate.TaskAddedExecutorEvent
import ru.quipy.aggregate.TaskNameUpdatedEvent
import ru.quipy.aggregate.TaskStatusChangedEvent
import ru.quipy.logic.UserAccessChecker.Companion.canAccess
import java.util.*

fun ProjectAggregateState.addTask(callerId: UUID, title: String, taskStatusId: UUID): TaskAddedEvent {
    canAccess(this, callerId)
    return TaskAddedEvent(UUID.randomUUID(), title, getId(), taskStatusId)
}

fun ProjectAggregateState.addTaskExecutor(callerId: UUID, taskId: UUID, userId: UUID): TaskAddedExecutorEvent {
    canAccess(this, callerId)
    canAccess(this, userId)

    if (tasks[taskId]?.assigneeId == userId) {
        throw IllegalArgumentException("Task with id=$taskId already assigned to userId=$userId")
    }

    return TaskAddedExecutorEvent(taskId, getId(), userId)
}

fun ProjectAggregateState.changeTaskTitle(callerId: UUID, taskId: UUID, title: String): TaskNameUpdatedEvent {
    canAccess(this, callerId)

    if (tasks[taskId] == null) {
        throw IllegalArgumentException("Task with id=$taskId is not in this project")
    }

    if (tasks[taskId]?.title == title) {
        throw IllegalArgumentException("Title for task with id=$taskId should be different")
    }

    return TaskNameUpdatedEvent(taskId, title)
}

fun ProjectAggregateState.changeTaskStatus(callerId: UUID, taskId: UUID, taskStatusId: UUID): TaskStatusChangedEvent {
    canAccess(this, callerId)

    if (!taskStatuses.containsKey(taskStatusId)) {
        throw IllegalArgumentException("Task status with id $taskStatusId does not exist")
    }

    if (tasks[taskId]?.taskStatusId == taskStatusId) {
        throw IllegalArgumentException("Status for task with id=$taskId should be different")
    }

    return TaskStatusChangedEvent(taskId, getId(), taskStatusId)
}