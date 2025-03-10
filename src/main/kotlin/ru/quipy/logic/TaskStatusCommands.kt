package ru.quipy.logic

import ru.quipy.aggregate.TaskStatusCreatedEvent
import ru.quipy.aggregate.TaskStatusDeletedEvent
import ru.quipy.logic.UserAccessChecker.Companion.checkUserAccess
import java.util.*


fun ProjectAggregateState.createTaskStatus(callerId: UUID, title: String, colorRgb: Int): TaskStatusCreatedEvent {
    checkUserAccess(this, callerId)
    return TaskStatusCreatedEvent(UUID.randomUUID(), getId(), title, colorRgb)
}

fun ProjectAggregateState.deleteTaskStatus(callerId: UUID, taskStatusId: UUID): TaskStatusDeletedEvent {
    checkUserAccess(this, callerId)
    if (hasTasksWithStatus(taskStatusId)) throw IllegalArgumentException("Status with id=$taskStatusId cannot be deleted since it has associated tasks")
    return TaskStatusDeletedEvent(taskStatusId, getId())
}

private fun ProjectAggregateState.hasTasksWithStatus(taskStatusId: UUID): Boolean {
    for (task in tasks) {
        if (task.value.taskStatusId == taskStatusId) {
            return true;
        }
    }

    return false;
}