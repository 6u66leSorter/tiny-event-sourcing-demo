package ru.quipy.projections.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProjectProjectionRepository : JpaRepository<ProjectData, UUID> { }