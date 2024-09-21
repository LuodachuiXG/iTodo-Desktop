package cc.loac.itodo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import cc.loac.itodo.data.models.Todo
import cc.loac.itodo.data.models.enums.TodoStatus
import cc.loac.itodo.ui.theme.DEFAULT_PADDING
import cc.loac.itodo.ui.theme.DEFAULT_SPACING
import cc.loac.itodo.util.format
import kotlinx.coroutines.launch

/**
 * 添加代办事项弹窗
 * @param visible 弹窗是否可见
 * @param modifier Modifier
 * @param onDismiss 弹窗关闭回调
 * @param onAdd 添加代办事项回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoDialog(
    visible: Boolean = false,
    modifier: Modifier = Modifier,
    snackBarHostState: SnackbarHostState,
    onDismiss: () -> Unit = {},
    onAdd: (todo: Todo) -> Unit
) {
    val scope = rememberCoroutineScope()

    // 代办事项标题（可空）
    var title by remember {
        mutableStateOf("")
    }

    // 代办事项内容
    var todoContent by remember {
        mutableStateOf("")
    }

    // 代办事项内容是否显示错误
    var todoContentError by remember {
        mutableStateOf(false)
    }

    // 是否显示截止日期选择框
    var showDeadlineDialog by remember {
        mutableStateOf(false)
    }

    // 截止日期选择状态
    val datePickerState = rememberDatePickerState()

    // 是否置顶
    var top by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(visible) {
        // 隐藏添加代办事项弹窗时清空数据
        if (!visible) {
            title = ""
            todoContent = ""
            todoContentError = false
            showDeadlineDialog = false
            datePickerState.selectedDateMillis = null
            top = false
        }
    }

    BaseDialog(
        visible = visible,
        onDismiss = onDismiss,
        dismissOnBackPress = false,
        dismissOnClickOutSide = false,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(DEFAULT_PADDING).verticalScroll(rememberScrollState())
        ) {
            // 弹窗标题
            Text(
                text = "添加 Todo",
                style = MaterialTheme.typography.titleLarge
            )

            // 弹窗内容
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DEFAULT_PADDING),
                verticalArrangement = Arrangement.spacedBy(DEFAULT_SPACING),
            ) {
                // 代办事项标题
                Input(
                    value = title,
                    label = "标题",
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth()
                )

                // 代办事项内容
                Input(
                    value = todoContent,
                    label = "代办事项",
                    onValueChange = { todoContent = it },
                    modifier = Modifier.fillMaxWidth(),
                    isRequired = true,
                    singleLine = false,
                    error = todoContentError,
                    onErrorCancel = {
                        todoContentError = false
                    }
                )

                // 截止日期
                val timeStr = if (datePickerState.selectedDateMillis == null) {
                    "无"
                } else {
                    datePickerState.selectedDateMillis!!.format("yyyy 年 MM 月 dd 日")
                }
                OutlinedTextField(
                    value = timeStr,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("截止日期")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Row {
                            if (datePickerState.selectedDateMillis != null) {
                                IconButton(
                                    onClick = { datePickerState.selectedDateMillis = null },
                                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "清空选择截止日期"
                                    )
                                }
                            }

                            IconButton(
                                onClick = { showDeadlineDialog = true },
                                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "选择截止日期"
                                )
                            }
                        }
                    }
                )

                // 显示截止日期选择框
                if (showDeadlineDialog) {
                    DatePickerDialog(
                        onDismissRequest = {
                            showDeadlineDialog = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDeadlineDialog = false
                                }
                            ) {
                                Text("确定")
                            }
                        },
                        content = {
                            DatePicker(
                                state = datePickerState
                            )
                        }
                    )
                }

                // 是否置顶
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(DEFAULT_SPACING)
                ) {
                    Text(
                        text = if (top) "置顶" else "正常",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Switch(
                        checked = top,
                        onCheckedChange = { top = it }
                    )
                }
            }

            // 弹窗按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(DEFAULT_SPACING, Alignment.End)
            ) {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("取消")
                }

                TextButton(
                    onClick = {
                        if (todoContent.isBlank()) {
                            scope.launch {
                                todoContentError = true
                                snackBarHostState.showSnackbar(
                                    "代办事项内容不能为空",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            return@TextButton
                        }

                        onAdd(
                            Todo(
                                title = title.ifBlank { null },
                                todo = todoContent,
                                status = TodoStatus.UNSTARTED,
                                deadline = datePickerState.selectedDateMillis,
                                top = top
                            )
                        )
                    }
                ) {
                    Text("保存")
                }
            }
        }
    }
}

/**
 * 输入框封装
 * @param value 文本内容
 * @param label 标签
 * @param modifier Modifier
 * @param singleLine 是否单行
 * @param onValueChange 文本内容改变回调
 * @param isRequired 是否必填
 * @param error 是否显示错误
 * @param onErrorCancel 错误取消回调
 */
@Composable
private fun Input(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit,
    isRequired: Boolean = false,
    error: Boolean = false,
    onErrorCancel: () -> Unit = {}
) {
    OutlinedTextField(
        modifier = modifier.onFocusChanged {
            if (it.isFocused) {
                onErrorCancel()
            }
        },
        singleLine = singleLine,
        value = value,
        onValueChange = onValueChange,
        isError = error,
        label = {
            Row {
                Text(label)
                if (isRequired) Text(
                    text = "（必填）",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = if (value.isNotEmpty()) {
            {
                IconButton(
                    onClick = {
                        onValueChange("")
                    },
                    modifier = Modifier
                        .size(26.dp)
                        .pointerHoverIcon(PointerIcon.Hand),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "清空$label",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        } else null
    )
}