package ru.quipy.projections.service

import org.springframework.stereotype.Service
import ru.quipy.aggregate.*
import ru.quipy.projections.data.TaskData
import ru.quipy.projections.repository.TaskProjectionRepository
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent
import java.util.*

@Service
@AggregateSubscriber(
    aggregateClass = ProjectAggregate::class, subscriberName = "task-projection"
)
class TaskService(
    private val taskProjectionRepository: TaskProjectionRepository
) {
    @SubscribeEvent
    fun newTask(event: TaskAddedEvent) {
        taskProjectionRepository.save(TaskData(event.taskId, event.projectId, event.title, UUID(0, 0), mutableSetOf()))
    }

    @SubscribeEvent
    fun changeName(event: TaskNameUpdatedEvent) {
        val data = taskProjectionRepository.findById(event.taskId).get()
        data.title = event.title
        taskProjectionRepository.save(data)
    }

    @SubscribeEvent
    fun changeStatus(event: TaskStatusChangedEvent) {
        val data = taskProjectionRepository.findById(event.taskId).get()
        data.taskStatusId = event.taskStatusId
        taskProjectionRepository.save(data)
    }

    @SubscribeEvent
    fun assignUser(event: TaskAddedExecutorEvent) {
        val data = taskProjectionRepository.findById(event.taskId).get()
        data.assignees.add(event.userId)
        taskProjectionRepository.save(data)
    }
}