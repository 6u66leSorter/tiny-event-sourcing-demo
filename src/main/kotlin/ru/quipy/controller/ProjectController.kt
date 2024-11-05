package ru.quipy.controller

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
import java.util.*

@RestController
@RequestMapping("/project")
class ProjectController(
    val projectEsService: EventSourcingService<UUID, ProjectAggregate, ProjectAggregateState>
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
}