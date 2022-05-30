package com.nexus.kointro.shared.repos

import com.nexus.kointro.shared.models.Todo
import kotlinx.coroutines.flow.Flow

interface TodosRepo {

    suspend fun addTodo(todo: Todo)

    fun getAllTodos(): Flow<List<Todo>>

    suspend fun deleteTodo(id: Long)

    suspend fun getTodoById(id: Long): Todo?
}