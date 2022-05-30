package com.nexus.kointro.core_features.add_edit_todo

sealed class AddEditTodoScreenEvent {
    data class TitleTextChanged(val title: String) : AddEditTodoScreenEvent()
    data class DescriptionTextChanged(val description: String) : AddEditTodoScreenEvent()
    object SaveButtonClicked : AddEditTodoScreenEvent()
}
