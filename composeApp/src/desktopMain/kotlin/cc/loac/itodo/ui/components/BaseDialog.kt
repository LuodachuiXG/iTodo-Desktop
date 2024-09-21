package cc.loac.itodo.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import cc.loac.itodo.ui.theme.DEFAULT_PADDING
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 基础 Dialog，默认有一个 Card 作为卡片的基底，同时有一些默认动画效果
 * @param visible 是否可见
 * @param onDismiss Dialog 关闭回调
 * @param modifier Modifier
 * @param content Dialog 内容
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BaseDialog(
    visible: Boolean = false,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnClickOutSide: Boolean = true,
    dismissOnBackPress: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val scope = rememberCoroutineScope()

    // Dialog 周围 Mask 颜色
    val scrimColorAnimate = animateColorAsState(
        targetValue = if (visible) Color.Black.copy(alpha = .6f) else Color.Transparent
    )

    // 显示 Dialog
    var showDialog by remember {
        mutableStateOf(false)
    }

    // 显示 Dialog 中的 Card
    var showCard by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(visible) {
        // 用于控制 Dialog 显示动画
        if (visible) {
            // 先显示 Dialog，然后再显示 Card
            scope.launch {
                showDialog = true
                delay(50)
                showCard = true
            }
        } else {
            // 隐藏 Card，然后再隐藏 Dialog
            scope.launch {
                showCard = false
                delay(350)
                showDialog = false
            }
        }
    }

    if (showDialog) {
        BasicAlertDialog(
            modifier = modifier,
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                scrimColor = scrimColorAnimate.value,
                dismissOnBackPress = dismissOnBackPress,
                dismissOnClickOutside = dismissOnClickOutSide
            )
        ) {
            AnimatedVisibility(
                visible = showCard,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Card(
                    modifier = Modifier.padding(DEFAULT_PADDING)
                ) {
                    content()
                }
            }
        }
    }
}