package ru.quipy.projections.data

import java.util.UUID
import javax.persistence.*

@Entity
data class TaskData (
    @Id
    val taskId: UUID,
    val projectId: UUID,
    var title: String,
    var taskStatusId: UUID,
    @ElementCollection(fetch = FetchType.EAGER)
    val assignees: MutableSet<UUID>
) {
    constructor() : this(UUID(0, 0), UUID(0, 0), "", UUID(0, 0), mutableSetOf())
}