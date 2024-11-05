package ru.quipy.logic

import java.util.*

class UserAccessChecker {
    companion object {
        fun canAccess(project: ProjectAggregateState, userId: UUID) {
            if (project.creatorId != userId && !project.memberIds.contains(userId)) {
                throw IllegalArgumentException("User with id=$userId cannot access project with id=${project.getId()}")
            }
        }
    }
}