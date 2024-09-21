package cc.loac.itodo.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cc.loac.itodo.data.models.Todo
import cc.loac.itodo.data.models.enums.TodoStatus
import cc.loac.itodo.ui.theme.*
import cc.loac.itodo.util.painter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        modifier = modifier.height(150.dp)
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

            // 操作按钮
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(SMALL, Alignment.End),
                    verticalAlignment = Alignment.Bottom,
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