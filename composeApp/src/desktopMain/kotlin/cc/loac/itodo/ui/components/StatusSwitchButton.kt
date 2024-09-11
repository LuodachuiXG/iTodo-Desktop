package cc.loac.itodo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cc.loac.itodo.data.models.enums.TodoStatus
import cc.loac.itodo.util.painter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 代办事项状态转换按钮
 * @param status 当前状态
 * @param modifier 修饰符
 * @param onClick 点击事件
 */
@Composable
fun StatusSwitchButton(
    status: TodoStatus,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    // 是否显示文字
    var showText by remember {
        mutableStateOf(false)
    }

    // 控制显示文字停止显示的 Job
    var job by remember {
        mutableStateOf<Job?>(null)
    }

    LaunchedEffect(status) {
        job?.let {
            it.cancel()
            job = null
        }
        job = scope.launch {
            showText = true
            // 3 秒后隐藏文字
            delay(3000)
            showText = false
        }
    }

    Row(
        modifier = modifier
            .defaultMinSize(28.dp, 28.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(2.dp))
        Icon(
            modifier = Modifier.size(24.dp),
            painter = if (status == TodoStatus.UNSTARTED) {
                painter("unstarted.svg")
            } else if (status == TodoStatus.PROGRESSING) {
                painter("progressing.svg")
            } else if (status == TodoStatus.COMPLETED && showText) {
                painter("thumb.svg")
            } else {
                painter("completed.svg")
            },
            contentDescription = status.getDescription(),
            tint = MaterialTheme.colorScheme.onPrimary
        )

        Row(
            modifier = Modifier
                .animateContentSize()
        ) {
            // 加 if 可以让进入时可以响应 Row 的宽度动画，但是消失时也可以及时响应
            // 否则会显先消失，然后 Row 才走动画，会有延迟
            if (showText) {
                AnimatedVisibility(
                    visible = true
                ) {
                    Text(
                        text = status.getDescription(),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = 6.dp, bottom = 2.dp, start = 2.dp)
                    )
                }
            }
        }
    }
}