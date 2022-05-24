package com.nexus.kointro.core_features.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nexus.kointro.core_features.todo_list.TodoListScreen
import com.nexus.kointro.core_features.todo_list.TodoListViewModel

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
    }
}