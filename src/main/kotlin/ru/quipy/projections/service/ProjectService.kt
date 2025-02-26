package ru.quipy.projections.service

import org.springframework.stereotype.Service
import ru.quipy.aggregate.ProjectAddedMemberEvent
import ru.quipy.aggregate.ProjectCreatedEvent
import ru.quipy.aggregate.TaskStatusCreatedEvent
import ru.quipy.aggregate.TaskStatusDeletedEvent
import ru.quipy.projections.data.ProjectData
import ru.quipy.projections.repository.ProjectProjectionRepository

@Service
class ProjectService(
    private val projectProjectionRepository: ProjectProjectionRepository
) {
    fun on(event: ProjectCreatedEvent) {
        val newProjectProjection = ProjectData(
            id = event.projectId,
            name = event.title,
            participants = mutableSetOf(event.assigneeId),
            statuses = mutableSetOf(),
            tasks = mutableSetOf()
        )

        projectProjectionRepository.save(newProjectProjection)
    }

    fun on(event: ProjectAddedMemberEvent) {
        projectProjectionRepository.findById(event.projectId).ifPresent {
            it.participants.add(event.assigneeId)
            projectProjectionRepository.save(it)
        }
    }

    fun on(event: TaskStatusCreatedEvent) {
        projectProjectionRepository.findById(event.projectId).ifPresent {
            it.statuses.add(event.taskStatusId)
            projectProjectionRepository.save(it)
        }
    }

    fun on(event: TaskStatusDeletedEvent) {
        projectProjectionRepository.findById(event.projectId).ifPresent {
            it.statuses.remove(event.taskStatusId)
            projectProjectionRepository.save(it)
        }
    }
}