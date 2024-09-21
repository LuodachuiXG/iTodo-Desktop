package cc.loac.itodo.ui.screens.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cc.loac.itodo.data.models.enums.KeyValueEnum
import cc.loac.itodo.data.sql.dao.KeyValueDao
import cc.loac.itodo.ui.components.TitleBar
import cc.loac.itodo.ui.theme.*
import cc.loac.itodo.util.painter
import com.materialkolor.ktx.lighten
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
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

    // 键值对表
    val keyValueDao = koinInject<KeyValueDao>()

    Box {
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


        // 是否显示主题名
        var showThemeName by remember {
            mutableStateOf(true)
        }

        // 主题名容器透明度动画
        val alphaAnimate = animateFloatAsState(
            targetValue = if (showThemeName) 1f else 0f
        )

        // 当前主题名（从数据库中取出）
        var currentThemeName by remember {
            mutableStateOf("芽绿")
        }

        // 当前主题颜色（从数据库中取出）
        var currentThemeColor by remember {
            mutableStateOf(Color(0xff96c24e))
        }

        LaunchedEffect(Unit) {
            // 获取数据库中存储的主题色
            val argb =
                keyValueDao.get(KeyValueEnum.THEME_COLOR_SEED, themeColors["芽绿"]!!.value.toString())!!.toULong()
            // 找到目标颜色
            val theme = themeColors.filter { it.value.value == argb }
            if (theme.isNotEmpty() && theme.size == 1) {
                currentThemeName = theme.keys.first()
                currentThemeColor = theme.values.first()
            }

            delay(800)
            showThemeName = false
        }

        // 进入主题页前先显示一下该文字
        AnimatedVisibility(
            visible = alphaAnimate.value > 0f,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .alpha(alphaAnimate.value),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentThemeName,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .background(
                            currentThemeColor,
                            shape = CardDefaults.shape
                        )
                        .shadow(
                            elevation = 40.dp,
                            shape = CardDefaults.shape,
                            ambientColor = currentThemeColor,
                            spotColor = currentThemeColor
                        )
                        .padding(DEFAULT_PADDING)
                )
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