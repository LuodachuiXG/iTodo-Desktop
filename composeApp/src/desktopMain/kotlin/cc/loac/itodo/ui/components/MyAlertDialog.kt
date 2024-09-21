package cc.loac.itodo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cc.loac.itodo.data.models.AlertFlowModel
import cc.loac.itodo.ui.theme.DEFAULT_PADDING
import cc.loac.itodo.ui.theme.DEFAULT_SPACING

/**
 * 自定义 AlertDialog
 * @param visible 是否显示
 * @param modifier 修饰符
 * @param alertFlowModel AlertFlowModel 数据类
 * @param onDismiss 关闭弹窗事件
 */
@Composable
fun MyAlertDialog(
    visible: Boolean = false,
    modifier: Modifier = Modifier,
    alertFlowModel: AlertFlowModel,
    onDismiss: () -> Unit = {}
) {
    BaseDialog(
        modifier = modifier,
        visible = visible,
        onDismiss = {
            alertFlowModel.onCancel()
        },
        dismissOnBackPress = false,
        dismissOnClickOutSide = false
    ) {
        Column(
            modifier = Modifier.padding(DEFAULT_PADDING)
        ) {
            // 标题
            Text(
                text = alertFlowModel.title,
                style = MaterialTheme.typography.titleLarge
            )

            // 弹窗内容
            Text(
                text = alertFlowModel.message,
                style = MaterialTheme.typography.bodyMedium
            )

            // 按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(DEFAULT_SPACING, Alignment.End)
            ) {
                alertFlowModel.cancelText?.let {
                    TextButton(
                        onClick = {
                            onDismiss()
                            alertFlowModel.onCancel()
                        }
                    ) {
                        Text(it)
                    }
                }

                TextButton(
                    onClick = {
                        onDismiss()
                        alertFlowModel.onConfirm()
                    }
                ) {
                    Text(alertFlowModel.confirmText)
                }
            }
        }
    }
}