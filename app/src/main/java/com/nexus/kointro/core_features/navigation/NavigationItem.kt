package com.nexus.kointro.core_features.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nexus.kointro.core_features.add_edit_todo.AddEditTodoScreen
import com.nexus.kointro.core_features.todo_list.TodoListScreen

sealed class NavigationItem(
    val route: String
) {
    object TodoListScreen : NavigationItem("/todo_list")
    object AddTodoScreen : NavigationItem("/add_todo_screen")
}

@Composable
fun KoinTroNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination= NavigationItem.TodoListScreen.route) {
        composable(route = NavigationItem.TodoListScreen.route) {
            TodoListScreen(navController = navController)
        }
        composable(
            route = "${NavigationItem.AddTodoScreen.route}?todoId={todoId}",
            arguments = listOf(
            navArgument("todoId") {
                type = NavType.LongType
                defaultValue = -1L
            }
        )) {
            AddEditTodoScreen(navController = navController)
        }
    }
}