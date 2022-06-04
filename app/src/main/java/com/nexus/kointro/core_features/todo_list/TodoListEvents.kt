package com.nexus.kointro.core_features.todo_list

import com.nexus.kointro.shared.models.Todo

sealed class TodoListEvents {
    data class OnCompleteStatusChanged(val completed: Boolean, val todo: Todo) : TodoListEvents()
    object OnAddToFavoritesClicked: TodoListEvents()
    object OnDeleteTodoClicked: TodoListEvents()
    data class OnTodoOptionsItemClicked(val todo: Todo) : TodoListEvents()
}
