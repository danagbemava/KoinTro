package com.nexus.kointro.shared.repos

import com.nexus.kointro.shared.models.Todo
import kotlinx.coroutines.flow.Flow

interface TodosRepo {

    fun addTodo(todo: Todo)

    fun getAllTodos(): Flow<List<Todo>>

    fun deleteTodo(id: Long)
}