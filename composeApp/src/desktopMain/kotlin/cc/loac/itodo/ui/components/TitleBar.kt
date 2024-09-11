package cc.loac.itodo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cc.loac.itodo.ui.theme.DEFAULT_PADDING
import cc.loac.itodo.util.painter

/**
 * 标题栏
 * @param title 标题
 * @param modifier 修饰符
 * @param showBack 是否显示返回按钮
 * @param endContent 右侧内容
 * @param onBackClick 返回按钮点击事件
 */
@Composable
fun TitleBar(
    title: String,
    modifier: Modifier = Modifier,
    showBack: Boolean = true,
    endContent: @Composable () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .height(80.dp)
            .fillMaxWidth()
            .padding(DEFAULT_PADDING),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Start
        ) {
            if (showBack) {
                IconButton(
                    onClick = {
                        onBackClick()
                    }
                ) {
                    Icon(
                        painter = painter("back.svg"),
                        contentDescription = "返回"
                    )
                }
            }
        }

        // 标题
        Row(
            modifier = Modifier.weight(2f),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        }

        // 右侧内容
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End
        ) {
            endContent()
        }
    }
}