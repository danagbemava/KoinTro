package com.nexus.kointro.core_features.todo_list

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.nexus.kointro.core_features.navigation.NavigationItem
import com.nexus.kointro.core_features.todo_list.components.TodoComponent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoListScreen(
    navController: NavController,
    todoListViewModel: TodoListViewModel = get()
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


    LaunchedEffect(key1 = true) {
        todoListViewModel.channelEvents.collect { event ->
            when(event) {
                is TodoListViewModel.UiEvent.HideBottomSheet -> {
                    sheetState.hide()
                }
                is TodoListViewModel.UiEvent.ShowBottomSheet -> {
                    sheetState.show()
                }
            }
        }
    }

    val todoListStates = todoListViewModel.todosState.collectAsState()

    return ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            ListItem(
                modifier = Modifier.clickable {
                   todoListViewModel.onEvent(TodoListEvents.OnAddToFavoritesClicked)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Add to Favorite Icon"
                    )
                },
                text = {
                    Text(text = "Add To Favorite")
                }
            )
            ListItem(
                modifier = Modifier.clickable {
                    todoListViewModel.onEvent(TodoListEvents.OnDeleteTodoClicked)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete todo Icon"
                    )
                },
                text = {
                    Text(text = "Delete Todo")
                }
            )
        }) {
        Scaffold(
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
            },
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

                            TodoComponent(
                                todo = todo,
                                onTodoItemClick = {
                                    val route =
                                        "${NavigationItem.AddTodoScreen.route}?todoId=${todo.id}"
                                    Log.d("TodoListScreen", "Navigating to $route")
                                    navController.navigate(route)
                                },
                                onCheckedChange = { changed ->
                                    todoListViewModel.onEvent(
                                        TodoListEvents.OnCompleteStatusChanged(changed, todo)
                                    )
                                },
                                onMoreClick = {
                                    todoListViewModel.onEvent(TodoListEvents.OnTodoOptionsItemClicked(todo))
                                }
                            )
                        }
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