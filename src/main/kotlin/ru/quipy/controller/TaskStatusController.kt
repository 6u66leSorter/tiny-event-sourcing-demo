package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.aggregate.ProjectAggregate
import ru.quipy.aggregate.TaskStatusCreatedEvent
import ru.quipy.aggregate.TaskStatusDeletedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.ProjectAggregateState
import ru.quipy.logic.createTaskStatus
import ru.quipy.logic.deleteTaskStatus
import java.util.*

@RestController
@RequestMapping("/projects/{projectId}/status")
class ProjectTaskStatusController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>
) {
    @PostMapping
    fun createTaskStatus(@PathVariable projectId: UUID, @RequestParam callerId: UUID, @RequestBody dto: CreateTaskStatusDto)
    : TaskStatusCreatedEvent {
        return projectEsService.update(projectId){
            it.createTaskStatus(callerId, dto.title, dto.colorRgb)
        }
    }

    data class CreateTaskStatusDto(
        val title: String,
        val colorRgb: Int
    )

    @DeleteMapping("/{taskStatusId}")
    fun deleteTaskStatus(@PathVariable projectId: UUID, @PathVariable taskStatusId: UUID, @RequestParam callerId: UUID)
    : TaskStatusDeletedEvent {
        return projectEsService.update(projectId){
            it.deleteTaskStatus(callerId, taskStatusId)
        }
    }
}