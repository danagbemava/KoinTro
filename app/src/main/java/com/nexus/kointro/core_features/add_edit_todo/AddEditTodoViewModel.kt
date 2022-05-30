package com.nexus.kointro.core_features.add_edit_todo

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexus.kointro.shared.models.Todo
import com.nexus.kointro.shared.repos.TodosRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class AddEditTodoViewModel(
    private val arguments: Bundle,
    private val todosRepo: TodosRepo
) : ViewModel() {

    private val _addEditTodoState = MutableStateFlow(AddEditTodoScreenState())
    val screenState: StateFlow<AddEditTodoScreenState>
        get() = _addEditTodoState

    var currentTodoId: Long? = null

    private val eventChannel = Channel<UIEvent>()
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        getExistingTodo()
    }

    private fun getExistingTodo() {
        viewModelScope.launch {
            val id = arguments.get("todoId") as Long?
            Log.d(TAG, "id: $id")
            id?.let {
                currentTodoId = it
                if(currentTodoId == -1L) {
                    _addEditTodoState.value = AddEditTodoScreenState()
                } else {
                    val todo = todosRepo.getTodoById(it)
                    _addEditTodoState.value = todo?.let { myTodo ->
                        AddEditTodoScreenState(
                            title = myTodo.name ?: "",
                            description = myTodo.description ?: "",
                            pageTitle = "Edit Todo"
                        )
                    } ?: AddEditTodoScreenState()
                }
            }
        }
    }

    fun onEvent(event: AddEditTodoScreenEvent) {
        when (event) {
            is AddEditTodoScreenEvent.TitleTextChanged -> {
                updateTitle(event.title)
            }
            is AddEditTodoScreenEvent.DescriptionTextChanged -> {
                updateDescription(event.description)
            }
            is AddEditTodoScreenEvent.SaveButtonClicked -> {
                saveTodo()
            }
        }
    }

    private fun updateTitle(title: String) {
       viewModelScope.launch {
           _addEditTodoState.value = _addEditTodoState.value.copy(title = title)
       }
    }

    private fun updateDescription(description: String) {
        viewModelScope.launch {
            _addEditTodoState.value = _addEditTodoState.value.copy(description = description)
        }
    }

    private fun saveTodo() {
        viewModelScope.launch {
            val todoValue = _addEditTodoState.value

            if(todoValue.title.isEmpty()) {
                eventChannel.send(UIEvent.TodoNotSaved("Please enter title"))
                return@launch
            }

            if(todoValue.description.isEmpty()) {
                eventChannel.send(UIEvent.TodoNotSaved("Please enter description"))
                return@launch
            }

            try {
              val todo = Todo(name = todoValue.title, description = todoValue.description)

                if(currentTodoId == null || currentTodoId == -1L) {
                    todosRepo.addTodo(todo)
                } else {
                    todo.id = currentTodoId!!
                    todosRepo.addTodo(todo)
                }
                Log.d(TAG, "Todo saved")
                eventChannel.send(UIEvent.TodoSaved)
            } catch (e: Exception) {
                eventChannel.send(UIEvent.TodoNotSaved("Error saving todo"))
            }
        }
    }

    sealed class UIEvent {
        object TodoSaved : UIEvent()
        data class TodoNotSaved(val error: String) : UIEvent()
    }

    companion object {
        const val TAG = "AddEditTodoViewModel"
    }
}