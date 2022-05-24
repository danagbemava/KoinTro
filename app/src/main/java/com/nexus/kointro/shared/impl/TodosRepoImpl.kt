package com.nexus.kointro.shared.impl

import com.nexus.kointro.shared.models.Todo
import com.nexus.kointro.shared.repos.TodosRepo
import io.objectbox.Box
import io.objectbox.BoxStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TodosRepoImpl(
    private val store: BoxStore
) : TodosRepo {

    private var _internalStore: Box<Todo> = store.boxFor(Todo::class.java)

    override fun addTodo(todo: Todo) {
        _internalStore.put(todo)
    }

    override fun getAllTodos(): Flow<List<Todo>> {
       return flow {
           _internalStore.all
       }
    }

    override fun deleteTodo(id: Long) {
       _internalStore.remove(id)
    }

}