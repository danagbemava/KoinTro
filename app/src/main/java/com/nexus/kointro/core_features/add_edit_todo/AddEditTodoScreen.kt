package com.nexus.kointro.core_features.add_edit_todo

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getStateViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AddEditTodoScreen(
    navController: NavController,
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val vm = getStateViewModel<AddEditTodoViewModel>(parameters = { parametersOf(navController.currentBackStackEntry?.arguments) })

    val states = vm.screenState.collectAsState()

    LaunchedEffect(true) {
        vm.eventsFlow.collect {
            when(it) {
                is AddEditTodoViewModel.UIEvent.TodoSaved -> {
                    navController.navigateUp()
                }
                is AddEditTodoViewModel.UIEvent.TodoNotSaved -> {
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = it.error,
                        )
                    }
                }
            }
        }
    }

    return Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar (
                title = {
                    Text(text = states.value.pageTitle)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                vm.onEvent(AddEditTodoScreenEvent.SaveButtonClicked)
            }) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Save Button")
            }
        }
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()) {
            OutlinedTextField(
                value = states.value.title,
                label = {
                        Text(text = "Title")
                },
                onValueChange = {
                    vm.onEvent(AddEditTodoScreenEvent.TitleTextChanged(it))
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = states.value.description,
                label = {
                    Text(text = "Description")
                },
                onValueChange = {
                    vm.onEvent(AddEditTodoScreenEvent.DescriptionTextChanged(it))
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

//
//@Preview
//@Composable
//fun AddEditTodoScreenPreview() {
//    AddEditTodoScreen(null, null)
//}