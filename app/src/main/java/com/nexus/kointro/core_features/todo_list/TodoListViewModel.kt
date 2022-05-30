package com.nexus.kointro.core_features.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        viewModelScope.launch {
            todosRepo.getAllTodos().collectLatest {
                if(it.isEmpty()){
                    _todosState.value = TodosListState.EmptyTodosState
                } else {
                    _todosState.value = TodosListState.NonEmptyTodosState(it)
                }
            }
        }

    }

    companion object {
        private const val TAG = "TodoListViewModel"
    }
}