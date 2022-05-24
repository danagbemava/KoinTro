package com.nexus.kointro.core_features.todo_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.kointro.shared.models.Todo
import com.nexus.kointro.shared.repos.TodosRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TodoListViewModel(
    private val todosRepo: TodosRepo
) : ViewModel() {

    private val _todosState = MutableStateFlow<TodosListState>(TodosListState.LoadingState)
    val todosState: StateFlow<TodosListState> = _todosState

    init {
        getTodos()
    }

    private fun getTodos() {
        todosRepo.getAllTodos().onEach { todos ->
            _todosState.value = TodosListState.NonEmptyTodosState(todos)
        }.onEmpty {
            _todosState.value = TodosListState.EmptyTodosState
        }.launchIn(viewModelScope)
    }

    companion object {
        private const val TAG = "TodoListViewModel"
    }
}