package cc.loac.itodo.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import cc.loac.itodo.data.models.Todo
import cc.loac.itodo.ui.theme.DEFAULT_PADDING
import cc.loac.itodo.ui.theme.DEFAULT_SPACING
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 添加代办事项弹窗
 * @param modifier Modifier
 * @param visible 弹窗是否可见
 * @param onDismiss 弹窗关闭回调
 * @param onAdd 添加代办事项回调
 */
@Composable
fun AddTodoDialog(
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    onDismiss: () -> Unit = {},
    onAdd: (todo: Todo) -> Unit
) {
    BaseDialog(
        visible = visible,
        onDismiss = onDismiss,
        dismissOnBackPress = false,
        dismissOnClickOutSide = false
    ) {
        Column(
            modifier = Modifier.padding(DEFAULT_PADDING)
        ) {
            // 弹窗标题
            Text(
                text = "添加 Todo",
                style = MaterialTheme.typography.titleLarge
            )

            // 弹窗内容

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

                    }
                ) {
                    Text("保存")
                }
            }
        }
    }
}