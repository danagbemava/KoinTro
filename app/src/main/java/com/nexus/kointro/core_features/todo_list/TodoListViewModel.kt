package com.nexus.kointro.core_features.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.kointro.shared.models.Todo
import com.nexus.kointro.shared.repos.TodosRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TodoListViewModel(
    private val todosRepo: TodosRepo
) : ViewModel() {

    private val _todosState = MutableStateFlow<TodosListState>(TodosListState.LoadingState)
    val todosState: StateFlow<TodosListState> = _todosState

    private val channel = Channel<UiEvent>()
    val channelEvents = channel.receiveAsFlow()

    private var currentTodo: Todo? = null

    init {
        getTodos()
    }

    fun onEvent(event: TodoListEvents) {
        when (event) {
            is TodoListEvents.OnCompleteStatusChanged -> {
                setCompletedStatus(event.completed, event.todo)
            }
            is TodoListEvents.OnAddToFavoritesClicked -> {
                setFavoritesStatus()
            }
            is TodoListEvents.OnDeleteTodoClicked -> {
                deleteTodo()
            }
            is TodoListEvents.OnTodoOptionsItemClicked -> {
                setCurrentTodo(event.todo)
            }
        }
    }

    private fun setCompletedStatus(completed: Boolean, todo: Todo) {
        viewModelScope.launch {
            val updatedTodo = todo.copy(completed = completed)
            todosRepo.addTodo(updatedTodo)
            getTodos()
        }
    }

    private fun setCurrentTodo(todo: Todo) {
        currentTodo = todo
        viewModelScope.launch {
            channel.send(UiEvent.ShowBottomSheet)
        }
    }

    private fun setFavoritesStatus() {
        viewModelScope.launch {
            currentTodo?.let {
                val updatedTodo = it.copy(isFavorite = !it.isFavorite)
                todosRepo.addTodo(updatedTodo)
                getTodos()
            }
        }
        clearCurrentTodo()
    }

    private fun deleteTodo() {
        viewModelScope.launch {
            currentTodo?.let {
                todosRepo.deleteTodo(it.id)
                getTodos()
            }
        }
        clearCurrentTodo()
    }

    private fun getTodos() {
        viewModelScope.launch {
            todosRepo.getAllTodos().collectLatest {
                if (it.isEmpty()) {
                    _todosState.value = TodosListState.EmptyTodosState
                } else {
                    _todosState.value = TodosListState.NonEmptyTodosState(it)
                }
            }
        }

    }

    private fun clearCurrentTodo() {
        currentTodo = null
        viewModelScope.launch {
            channel.send(UiEvent.HideBottomSheet)
        }
    }

    companion object {
        private const val TAG = "TodoListViewModel"
    }

    sealed class UiEvent {
        object HideBottomSheet : UiEvent()
        object ShowBottomSheet : UiEvent()
    }
}