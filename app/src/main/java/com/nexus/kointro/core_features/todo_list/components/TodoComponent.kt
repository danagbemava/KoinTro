package com.nexus.kointro.core_features.todo_list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.nexus.kointro.shared.models.Todo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoComponent(
    todo: Todo,
    onTodoItemClick: () -> Unit,
    onMoreClick: () -> Unit,
    onCheckedChange: (checked: Boolean) -> Unit,
) {
    return ListItem(
            modifier = Modifier
                .clickable { onTodoItemClick() }
                .fillMaxWidth(),
            icon = {
                Checkbox(checked = todo.completed, onCheckedChange = onCheckedChange)
            },
            text = {
                Text(text = "${todo.name}", maxLines = 1)
            },
            secondaryText = {
                Text(text = "${todo.description}", maxLines = 3)
            },
            trailing = {
                IconButton(
                    onClick = onMoreClick
            ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Extra options",
                    )
                }
            }
        )
}