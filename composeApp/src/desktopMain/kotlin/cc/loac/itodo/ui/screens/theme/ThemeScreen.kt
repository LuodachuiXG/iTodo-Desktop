package cc.loac.itodo.ui.screens.theme

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cc.loac.itodo.ui.components.TitleBar
import cc.loac.itodo.ui.theme.*
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


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(DEFAULT_PADDING),
            horizontalArrangement = Arrangement.spacedBy(DEFAULT_SPACING),
            verticalArrangement = Arrangement.spacedBy(DEFAULT_SPACING),
            modifier = Modifier.fillMaxSize()
        ) {
            items(themeColors.size) { index ->
                val color = themeColors.entries.elementAt(index)
                ColorButton(
                    color = color.value,
                    name = color.key
                ) {
                    scope.launch {
                        Theme.themeFlow.emit(color.value)
                        vm.setThemeSeedColor(color.value)
                    }
                }
            }
        }
    }
}

/**
 * 颜色卡片
 * @param color 颜色
 * @param name 颜色名
 * @param modifier 修饰符
 * @param onClick 点击事件
 */
@Composable
fun ColorButton(
    color: Color,
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    // 是否显示选中的图标，用于再点击后短暂显示选中的图标
    var showIcon by remember {
        mutableStateOf(false)
    }
    // 控制上面的 showIcon 定时关闭的协程
    var job by remember {
        mutableStateOf<Job?>(null)
    }
    Box(
        modifier = modifier
            .height(60.dp)
            .fillMaxWidth()
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
        // 颜色名
        AnimatedVisibility(
            visible = !showIcon,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Text(
                text = name,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        // 选中图标
        AnimatedVisibility(
            visible = showIcon,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Icon(
                painter = painter("finish.svg"),
                contentDescription = "切换主题完成",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}