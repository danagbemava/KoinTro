package com.nexus.kointro.core_features.todo_list

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.nexus.kointro.core_features.navigation.NavigationItem
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoListScreen(
    navController: NavController,
    todoListViewModel: TodoListViewModel = get()
) {
    val scaffoldState = rememberScaffoldState()

    val todoListStates = todoListViewModel.todosState.collectAsState()

    return Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Koin Todos")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(NavigationItem.AddTodoScreen.route)
            }) {
               Icon(imageVector = Icons.Default.Add, contentDescription = "Add Todo Button")
            }
        }
    ) {
        when (todoListStates.value) {
            TodosListState.LoadingState -> {
                 LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            TodosListState.EmptyTodosState -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(text = "You have no todos")
                }
            }
            else -> {
                val todos = (todoListStates.value as TodosListState.NonEmptyTodosState).todos
                LazyColumn {
                    items(todos) { todo ->
                        ListItem(
                            modifier = Modifier.clickable {
                                val route = "${NavigationItem.AddTodoScreen.route}?todoId=${todo.id}"
                                Log.d("TodoListScreen", "Navigating to $route")
                               navController.navigate(route)
                            },
                            text = {
                                Text(text = "${todo.name}")
                            },
                            secondaryText = {
                                Text(text = "${todo.description}")
                            },
                        )
                    }
                }
            }
        }
    }
}

//
//@Preview
//@Composable
//fun TodoListScreenPreview() {
//    TodoListScreen(null)
//}