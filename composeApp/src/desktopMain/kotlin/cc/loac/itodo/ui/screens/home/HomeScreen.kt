package cc.loac.itodo.ui.screens.home

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cc.loac.itodo.data.models.Todo
import cc.loac.itodo.data.models.enums.ScreenWidth
import cc.loac.itodo.ui.components.AddTodoDialog
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

    // 代办事项列表
    val todoList = vm.todoList.collectAsState()

    // 是否显示添加代办事项 Dialog
    var showAddTodoDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        // 获取所有代办事项
        vm.getTodoList()
    }

    AddTodoDialog(
        visible = showAddTodoDialog,
        onDismiss = {
            showAddTodoDialog = false
        }
    ) {}


    Column(
        modifier = Modifier.padding(DEFAULT_PADDING)
    ) {
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
                horizontalArrangement = Arrangement.spacedBy(DEFAULT_SPACING, Alignment.Start)
            ) {
                items(todoList.value.size) {
                    val currentTodo = todoList.value[it]
                    TodoCard(
                        // 最后一个卡片添加底部边距，不要和悬浮按钮重叠
                        modifier = Modifier.padding(
                            bottom = if (it == todoList.value.lastIndex) 70.dp else 0.dp
                        ),
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

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.spacedBy(DEFAULT_SPACING),
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
//                        vm.insertTodo(
//                            Todo(
//                                title = "Hello ",
//                                todo = "你好这是你的第 ${todoList.value.size + 1} 条 Todo"
//                            )
//                        )
                        showAddTodoDialog = true
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "添加 Todo"
                        )
                    },
                    text = {
                        Text("添加")
                    }
                )

                FloatingActionButton(
                    onClick = {
                        vm.getTodoList()
                        scope.launch {
                            snackBar.showSnackbar("刷新成功", duration = SnackbarDuration.Short)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "刷新代办事项"
                    )
                }
            }
        }
    }
}