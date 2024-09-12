package cc.loac.itodo.ui.screens.home

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cc.loac.itodo.data.models.Todo
import cc.loac.itodo.data.models.enums.ScreenWidth
import cc.loac.itodo.ui.components.TodoCard
import cc.loac.itodo.ui.theme.*
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/**
 * 首页 Screen
 * @param navController 导航控制器
 * @param snackBar SnackBar控制器
 * @param screenWidth 屏幕宽度枚举
 * @param vm ViewModel
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    snackBar: SnackbarHostState,
    screenWidth: ScreenWidth,
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
                            title = "Hello ",
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

        val lazyGridSate = rememberLazyGridState()
        Box {
            LazyVerticalGrid(
                columns = GridCells.Fixed(
                    when (screenWidth) {
                        ScreenWidth.NORMAL -> 1
                        ScreenWidth.WIDE -> 2
                        ScreenWidth.EXTRA_WIDE -> 3
                        ScreenWidth.ULTRA_WIDE -> 4
                    }
                ),
                modifier = Modifier
                    .padding(top = SMALL)
                    .clip(RoundedCornerShape(VERY_SMALL)),
                verticalArrangement = Arrangement.spacedBy(DEFAULT_SPACING),
                state = lazyGridSate,
                horizontalArrangement = Arrangement.spacedBy(DEFAULT_PADDING, Alignment.Start)
            ) {
                items(todoList.value.size) {
                    val currentTodo = todoList.value[it]
                    TodoCard(
                        todoItem = currentTodo,
                        onDelete = {
                            // 删除事件
                            vm.deleteTodo(listOf(currentTodo.id)) { result ->
                                if (result) {
                                    // 删除成功，刷新列表
                                    vm.getTodoList()
                                }
                            }
                        }
                    ) { status ->
                        // 状态改变事件
                        vm.updateTodo(currentTodo.copy(status = status)) { result ->
                            if (result) {
                                // 更新成功，刷新列表
                                vm.getTodoList()
                            }
                        }
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(VERY_SMALL)
                    .align(Alignment.CenterEnd)
                    .offset(x = SMALL - 1.dp),
                adapter = rememberScrollbarAdapter(lazyGridSate),
                style = LocalScrollbarStyle.current.copy(
                    unhoverColor = MaterialTheme.colorScheme.surfaceVariant,
                    hoverColor = MaterialTheme.colorScheme.primary
                )
            )
        }

    }
}