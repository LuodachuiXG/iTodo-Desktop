package cc.loac.itodo.ui.components

import androidx.compose.animation.*
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
import cc.loac.itodo.util.painter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 删除按钮
 * @param modifier 修饰符
 * @param delay 延迟时间
 * @param onDelete 删除回调时间（需要短时间内点击两次才会触发此时间）
 */
@Composable
fun DeleteButton(
    modifier: Modifier = Modifier,
    delay: Long = 3000,
    onDelete: () -> Unit
) {
    // 是否已经点过一次
    var isClicked by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    var job by remember {
        mutableStateOf<Job?>(null)
    }

    val background by animateColorAsState(
        targetValue = if (isClicked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.errorContainer
    )
    val color by animateColorAsState(
        targetValue = if (isClicked) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onErrorContainer
    )

    Row(
        modifier = modifier
            .defaultMinSize(28.dp, 28.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(background)
            .clickable {
                if (isClicked) {
                    onDelete()
                    isClicked = false
                } else {
                    isClicked = true
                    job?.let {
                        it.cancel()
                        job = null
                    }
                    job = scope.launch {
                        // 延迟后重置点击
                        delay(delay)
                        isClicked = false
                    }
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            modifier = Modifier.size(20.dp),
            painter = when (isClicked) {
                true -> painter("delete_confirm.svg")
                else -> painter("delete.svg")
            },
            contentDescription = "删除代办事项",
            tint = color
        )

        Row(
            modifier = Modifier.animateContentSize()
        ) {
            // 加 if 可以让进入时可以响应 Row 的宽度动画，但是消失时也可以及时响应
            // 否则会显先消失，然后 Row 才走动画，会有延迟
            if (isClicked) {
                AnimatedVisibility(
                    visible = true
                ) {
                    Text(
                        text = "再次点击删除",
                        color = color,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(end = 6.dp, bottom = 2.dp, start = 2.dp)
                    )
                }
            }
        }
    }
}