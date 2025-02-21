package ru.quipy.controller

import org.springframework.web.bind.annotation.*
import ru.quipy.aggregate.ProjectAggregate
import ru.quipy.aggregate.TaskAddedEvent
import ru.quipy.aggregate.TaskAddedExecutorEvent
import ru.quipy.aggregate.TaskNameUpdatedEvent
import ru.quipy.aggregate.TaskStatusChangedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.*
import java.util.*

@RestController
@RequestMapping("/project/{projectId}/task")
class TaskController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>
) {
    @PostMapping
    fun createTask(@PathVariable projectId: UUID, @RequestParam callerId: UUID, @RequestBody dto: CreateTaskDto)
    : TaskAddedEvent {
        return projectEsService.update(projectId){
            it.addTask(callerId, dto.title, dto.taskStatusId)
        }
    }

    data class CreateTaskDto(
        val title: String,
        val taskStatusId: UUID
    )

    @PostMapping("/{taskId}/addExecutor")
    fun addExecutor(@PathVariable projectId: UUID, @PathVariable taskId: UUID, @RequestParam callerId: UUID, @RequestParam userId: UUID
    ): TaskAddedExecutorEvent {
        return projectEsService.update(projectId){
            it.addTaskExecutor(callerId, taskId, userId)
        }
    }

    @PatchMapping("/{taskId}/title")
    fun changeTaskTitle(@PathVariable projectId: UUID, @PathVariable taskId: UUID, @RequestParam callerId: UUID, @RequestParam title: String)
            : TaskNameUpdatedEvent {
        return projectEsService.update(projectId){
            it.changeTaskTitle(callerId, taskId, title)
        }
    }

    @PatchMapping("/{taskId}/status")
    fun changeTaskStatus(@PathVariable projectId: UUID, @PathVariable taskId: UUID, @RequestParam callerId: UUID, @RequestParam taskStatusId: UUID)
    : TaskStatusChangedEvent {
        return projectEsService.update(projectId){
            it.changeTaskStatus(callerId, taskId, taskStatusId)
        }
    }
}