package ru.quipy.projections.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.quipy.projections.data.TaskData
import java.util.UUID

@Repository
interface TaskProjectionRepository : JpaRepository<TaskData, UUID> {
    fun findByProjectId(projectId: UUID): List<TaskData>
}