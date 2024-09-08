package cc.loac.itodo.ui.screens.home

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import cc.loac.itodo.data.models.Todo
import cc.loac.itodo.ui.theme.DEFAULT_PADDING
import cc.loac.itodo.ui.theme.SMALL
import cc.loac.itodo.ui.theme.VERY_SMALL
import cc.loac.itodo.util.format
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/**
 * 首页 Screen
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    snackBar: SnackbarHostState,
    vm: HomeViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()

    val todoList = vm.todoList.collectAsState()

    LaunchedEffect(Unit) {
        vm.getTodoList()
    }

    Column(
        modifier = Modifier.padding(DEFAULT_PADDING)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SMALL)
        ) {
            Button(
                onClick = {
                    vm.getTodoList()
                    scope.launch {
                        snackBar.showSnackbar("刷新成功啦")
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("刷新")
            }

            OutlinedButton(
                onClick = {
                    vm.insertTodo(
                        Todo(
                            todo = "你好这是你的第 ${todoList.value.size + 1} 条 Todo"
                        )
                    )
                    scope.launch {
                        snackBar.showSnackbar("插入成功啦")
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("插入新 Todo")
            }
        }

        val state = rememberLazyListState()
        Box {
            LazyColumn(
                modifier = Modifier.padding(top = SMALL),
                verticalArrangement = Arrangement.spacedBy(SMALL),
                state = state
            ) {
                items(todoList.value.size) {
                    val todo = todoList.value[it]
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(DEFAULT_PADDING)) {
                            Text(
                                text = todo.todo,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                text = todo.createTime.format(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(VERY_SMALL)
                    .align(Alignment.CenterEnd)
                    .offset(x = VERY_SMALL),
                adapter = rememberScrollbarAdapter(state)
            )
        }

    }
}