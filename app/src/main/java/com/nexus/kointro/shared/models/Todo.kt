package com.nexus.kointro.shared.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Todo(
    @Id
    var id: Long = 0,
    val name: String?,
    val description: String?,
    val completed: Boolean = false,
    val isFavorite: Boolean = false,
)
