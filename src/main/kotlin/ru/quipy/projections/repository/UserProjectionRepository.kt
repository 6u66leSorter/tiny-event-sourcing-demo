package ru.quipy.projections.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.quipy.projections.data.UserData

@Repository
interface UserProjectionRepository : JpaRepository<UserData, String> {
    fun findByNickname(nickname: String): UserData?
}