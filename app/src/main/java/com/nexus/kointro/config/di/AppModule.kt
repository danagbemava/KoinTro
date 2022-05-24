package com.nexus.kointro.config.di

import com.nexus.kointro.core_features.todo_list.TodoListViewModel
import com.nexus.kointro.shared.data_source.LocalDataSource
import com.nexus.kointro.shared.impl.TodosRepoImpl
import com.nexus.kointro.shared.repos.TodosRepo
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<TodosRepo> { TodosRepoImpl(LocalDataSource.store) }

    viewModel {
        TodoListViewModel(get())
    }
}