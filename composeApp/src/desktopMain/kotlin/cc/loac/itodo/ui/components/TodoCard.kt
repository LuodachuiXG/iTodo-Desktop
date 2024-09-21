package cc.loac.itodo.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cc.loac.itodo.data.models.Todo
import cc.loac.itodo.data.models.enums.TodoStatus
import cc.loac.itodo.ui.theme.*
import cc.loac.itodo.util.calculateDays
import cc.loac.itodo.util.currentEightHourTimeStamp
import cc.loac.itodo.util.format
import kotlinx.coroutines.delay

/**
 * 代办事项卡片
 * @param todoItem 代办事项数据类
 * @param modifier 修饰符
 * @param onDelete 删除事件
 * @param onStatusChange 状态切换事件
 */
@Composable
fun TodoCard(
    todoItem: Todo,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    onStatusChange: (TodoStatus) -> Unit = {}
) {
    var status by remember {
        mutableStateOf(todoItem.status)
    }

    LaunchedEffect(todoItem) {
        status = todoItem.status
    }

    val textColor = animateColorAsState(
        targetValue = when (status) {
            TodoStatus.UNSTARTED -> Color.Unspecified
            TodoStatus.PROGRESSING -> MaterialTheme.colorScheme.primary
            TodoStatus.COMPLETED -> MaterialTheme.colorScheme.secondary
        }
    )
    Card(
        modifier = modifier.height(170.dp)
    ) {
        Column(
            modifier = Modifier.padding(DEFAULT_PADDING)
        ) {
            // 标题
            todoItem.title?.let {
                Text(
                    text = todoItem.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = if (status == TodoStatus.PROGRESSING) {
                        FontWeight.Bold
                    } else null,
                    // 删除线
                    textDecoration = if (status == TodoStatus.COMPLETED) {
                        TextDecoration.Underline
                    } else null,
                    color = textColor.value,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(DEFAULT_SPACING))
            }

            // 代办事项
            Text(
                text = todoItem.todo,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (todoItem.title == null && status == TodoStatus.PROGRESSING) {
                    FontWeight.Bold
                } else null,
                // 删除线
                textDecoration = if (status == TodoStatus.COMPLETED) {
                    TextDecoration.Underline
                } else null,
                color = textColor.value,
                maxLines = if (todoItem.title == null) 3 else 2,
                overflow = TextOverflow.Ellipsis
            )

            // 截止日期
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(top = DEFAULT_SPACING)
            ) {
                if (todoItem.deadline != null) {
                    // 是否小于 3 天
                    val lessThanThreeDays = todoItem.deadline - currentEightHourTimeStamp() <= 259_200_000L
                    // 是否小于 1 天
                    val lessThanOneDay = todoItem.deadline - currentEightHourTimeStamp() <= 86_400_000L
                    // 截止日期是否已过
                    val deadlinePassed = todoItem.deadline < currentEightHourTimeStamp()

                    val textStyle = MaterialTheme.typography.bodySmall.copy(
                        // 如果截止日期小于 3 天，显示红色
                        color = if (lessThanThreeDays && !deadlinePassed) {
                            MaterialTheme.colorScheme.error
                        } else {
                            Color.Unspecified
                        },
                        // 如果截止日期小于等于一天，加粗字体
                        fontWeight = if (lessThanOneDay && !deadlinePassed) {
                            FontWeight.Bold
                        } else null,
                        // 如果截止日期已过，添加删除线
                        textDecoration = if (deadlinePassed) {
                            TextDecoration.Underline
                        } else null
                    )

                    // 是否显示剩余天数
                    var showRemainingDays by remember {
                        mutableStateOf(false)
                    }

                    LaunchedEffect(Unit) {
                        // 如果还未过期，就循环切换显示剩余天数和具体时间
                        if (!deadlinePassed) {
                            while (true) {
                                delay(5000)
                                showRemainingDays = !showRemainingDays
                            }
                        }
                    }

                    Text(
                        text = "截止时间：",
                        // 提示文字不显示线
                        style = textStyle.copy(textDecoration = null)
                    )

                    Box {
                        // 显示剩余天数
                        androidx.compose.animation.AnimatedVisibility(
                            visible = showRemainingDays,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut()
                        ) {
                            // 剩余天数
                            val remainingDays = (todoItem.deadline - System.currentTimeMillis()).calculateDays()
                            Text(
                                text = if (remainingDays <= 1) "小于 1 天" else "不到 $remainingDays 天",
                                style = textStyle
                            )
                        }


                        // 显示具体时间
                        androidx.compose.animation.AnimatedVisibility(
                            visible = !showRemainingDays,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut()
                        ) {
                            Text(
                                text = todoItem.deadline.format("yyyy 年 MM 月 dd 日"),
                                style = textStyle
                            )
                        }
                    }
                }
            }

            // 操作按钮
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(SMALL, Alignment.End),
                ) {
                    // 代办事项状态切换按钮
                    StatusSwitchButton(
                        status = status
                    ) {
                        status = when (status) {
                            TodoStatus.UNSTARTED -> TodoStatus.PROGRESSING
                            TodoStatus.PROGRESSING -> TodoStatus.COMPLETED
                            TodoStatus.COMPLETED -> TodoStatus.UNSTARTED
                        }
                        onStatusChange(status)
                    }

                    // 代办事项删除按钮
                    DeleteButton {
                        onDelete()
                    }
                }
            }
        }

    }
}