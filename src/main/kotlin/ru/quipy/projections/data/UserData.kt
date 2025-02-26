package ru.quipy.projections.data

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user_projections")
data class UserData(
    @Id
    val userId: String,
    val nickname: String,
    val userName: String
) {
    constructor() : this("", "", "")
}