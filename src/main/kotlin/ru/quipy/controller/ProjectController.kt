package ru.quipy.controller

import liquibase.pro.packaged.it
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.quipy.aggregate.ProjectAggregate
import ru.quipy.aggregate.ProjectAddedMemberEvent
import ru.quipy.aggregate.ProjectCreatedEvent
import ru.quipy.aggregate.ProjectRenamedEvent
import ru.quipy.core.EventSourcingService
import ru.quipy.logic.ProjectAggregateState
import ru.quipy.logic.addMember
import ru.quipy.logic.create
import ru.quipy.logic.rename
import ru.quipy.projections.data.ProjectData
import ru.quipy.projections.data.TaskData
import ru.quipy.projections.repository.ProjectProjectionRepository
import ru.quipy.projections.repository.TaskProjectionRepository
import java.util.*

@RestController
@RequestMapping("/project")
class ProjectController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>,
    val taskProjectionRepository: TaskProjectionRepository,
    val projectProjectionRepository: ProjectProjectionRepository
) {
    @PostMapping
    fun createProject(@RequestParam title: String, @RequestParam callerId: UUID) : ProjectCreatedEvent {
        return projectEsService.create{
            it.create(callerId, title)
        }
    }

    @PatchMapping("/{projectId}/changeName")
    fun rename(@PathVariable projectId: UUID, @RequestParam title: String, @RequestParam callerId: UUID)
    : ProjectRenamedEvent {
        return projectEsService.update(projectId){
            it.rename(callerId, title)
        }
    }

    @PostMapping("/{projectId}/addMember")
    fun addMember(@PathVariable projectId: UUID, @RequestParam callerId: UUID, @RequestParam userId: UUID
    ): ProjectAddedMemberEvent {
        return projectEsService.update(projectId){
            it.addMember(userId, callerId)
        }
    }

    @GetMapping("/{projectId}")
    fun getProject(@PathVariable projectId: UUID) : ProjectAggregateState? {
        return projectEsService.getState(projectId)
    }

    @GetMapping("/{projectId}/tasks")
    fun getTasksInProject(@PathVariable projectId: UUID): ResponseEntity<List<TaskData>> {
        return ResponseEntity.ok(taskProjectionRepository.findByProjectId(projectId))
    }

    @GetMapping("/{projectId}/tasksByStatus")
    fun getTasksByStatus(@PathVariable projectId: UUID): ResponseEntity<Map<UUID, List<TaskData>>> {
        projectEsService.getState(projectId)
        return ResponseEntity.ok(taskProjectionRepository.findByProjectId(projectId).groupBy { it.taskStatusId })
    }

    @GetMapping("details/{projectId}")
    fun getProjectByID(@PathVariable projectId: UUID): ResponseEntity<ProjectData> {
        val projectProjection = projectProjectionRepository.findById(projectId).orElse(null)
        return if (projectProjection != null) {
            ResponseEntity.ok(projectProjection)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}