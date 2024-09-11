package cc.loac.itodo.ui.screens.theme

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cc.loac.itodo.ui.components.TitleBar
import cc.loac.itodo.ui.theme.DEFAULT_PADDING
import cc.loac.itodo.ui.theme.DEFAULT_SPACING
import cc.loac.itodo.ui.theme.ThemeFlow
import cc.loac.itodo.util.painter
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

/**
 * 主题 Screen
 */
@Composable
fun ThemeScreen(
    navController: NavHostController,
    snackBar: SnackbarHostState,
    vm: ThemeViewModel = koinViewModel()
) {
    val scope = rememberCoroutineScope()
    Column {
        TitleBar(
            title = "主题设置"
        ) {
            // 返回上一页
            navController.popBackStack()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DEFAULT_PADDING)
        ) {
            ColorButton(
                color = Color(0xFFB33B15)
            ) {
                scope.launch {
                    ThemeFlow.themeFlow.emit("red")
                }
            }

            ColorButton(
                color = Color(0xFF63A002)
            ) {
                scope.launch {
                    ThemeFlow.themeFlow.emit("green")
                }
            }

            ColorButton(
                color = Color(0xFF769CDF)
            ) {
                scope.launch {
                    ThemeFlow.themeFlow.emit("blue")
                }
            }

            ColorButton(
                color = Color(0xFFFFDE3F)
            ) {
                scope.launch {
                    ThemeFlow.themeFlow.emit("yellow")
                }
            }
        }
    }
}

/**
 * 颜色卡片
 * @param color 颜色
 * @param modifier 修饰符
 * @param onClick 点击事件
 */
@Composable
fun ColorButton(
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var showIcon by remember {
        mutableStateOf(false)
    }
    var job by remember {
        mutableStateOf<Job?>(null)
    }
    Box(
        modifier = modifier
            .height(60.dp)
            .fillMaxWidth()
            .padding(bottom = DEFAULT_SPACING)
            .clip(CardDefaults.shape)
            .background(color)
            .clickable {
                onClick()
                job?.let {
                    it.cancel()
                    job = null
                }
                job = scope.launch {
                    showIcon = true
                    delay(1500)
                    showIcon = false
                    cancel()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = showIcon,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Icon(
                painter = painter("finish.svg"),
                contentDescription = "切换完成",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}