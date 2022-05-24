package com.nexus.kointro.core_features.todo_list

import com.nexus.kointro.shared.models.Todo

sealed class TodosListState {
    object LoadingState: TodosListState()
    data class NonEmptyTodosState(val todos: List<Todo>) : TodosListState()
    object EmptyTodosState : TodosListState()
}