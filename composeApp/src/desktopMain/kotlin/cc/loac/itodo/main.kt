package cc.loac.itodo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cc.loac.itodo.data.koin.koinConfiguration
import cc.loac.itodo.data.models.enums.Key
import cc.loac.itodo.data.sql.DatabaseSingleton
import cc.loac.itodo.data.sql.dao.KeyValueDao
import cc.loac.itodo.ui.ITodoApp
import cc.loac.itodo.ui.theme.*
import cc.loac.itodo.util.painter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

private val ioScope = CoroutineScope(Dispatchers.IO)

fun main() {
    // 初始化数据库
    DatabaseSingleton.init()

    application {
        // 配置 Koin 注入
        KoinApplication(::koinConfiguration) {
            // 键值对表
            val keyValueDao = koinInject<KeyValueDao>()

            // 是否暗黑主题
            var isDark by remember {
                mutableStateOf<Boolean?>(null)
            }

            // 当前页面名
            var screenName by remember {
                mutableStateOf("")
            }

            // 窗口是否总是在最前
            var alwaysOnTop by remember {
                mutableStateOf(false)
            }

            // 窗口状态
            val windowState = rememberWindowState(width = 400.dp, height = 500.dp)

            // 标记当前窗口是否显示，在所有前置操作完成后显示
            var isVisible by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(Unit) {
                // 启动时获取一下之前的设置
                ioScope.launch {
                    // 是否暗黑模式
                    isDark = keyValueDao.getBoolean(Key.IS_DARK_MODE, false)!!

                    // 窗口大小
                    val size = keyValueDao.get(Key.WINDOW_SIZE, "400|500")!!
                    val width = size.substringBefore("|").toIntOrNull() ?: 400
                    val height = size.substringAfter("|").toIntOrNull() ?: 500
                    windowState.size = DpSize(width.dp, height.dp)

                    // 窗口位置
                    val position = keyValueDao.get(Key.WINDOW_POSITION, "40|40")!!
                    val x = position.substringBefore("|").toIntOrNull() ?: 40
                    val y = position.substringAfter("|").toIntOrNull() ?: 40
                    windowState.position = WindowPosition(x.dp, y.dp)
                    isVisible = true
                }
            }

            /**
             * 退出 APP
             */
            fun exitApp() {
                // 获取一下窗口的大小和位置保存到数据库
                ioScope.launch {
                    val size = "${windowState.size.width.value.toInt()}|${windowState.size.height.value.toInt()}"
                    val position = "${windowState.position.x.value.toInt()}|${windowState.position.y.value.toInt()}"
                    keyValueDao.set(Key.WINDOW_SIZE, size)
                    keyValueDao.set(Key.WINDOW_POSITION, position)

                    exitApplication()
                }
            }

            Window(
                onCloseRequest = ::exitApp,
                title = "iTodo${if (screenName.isBlank()) "" else " - $screenName"}",
                undecorated = true,
                transparent = true,
                visible = isVisible,
                state = windowState,
                alwaysOnTop = alwaysOnTop
            ) {
                isDark?.let { dark ->
                    ITodoTheme(
                        darkTheme = dark
                    ) {
                        Column {
                            WindowDraggableArea {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(VERY_SMALL)
                                        .border(
                                            width = 1.dp,
                                            color = if (dark) darkScheme.outline else lightScheme.outline,
                                            shape = RoundedCornerShape(SMALL)
                                        )
                                        .shadow(
                                            elevation = 2.dp,
                                            shape = RoundedCornerShape(SMALL),
                                            ambientColor = Color.Black,
                                            spotColor = Color.Black
                                        )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                if (dark) darkScheme.background else lightScheme.background
                                            )
                                            .padding(DEFAULT_PADDING),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "iTodo${if (screenName.isBlank()) "" else " - $screenName"}",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = if (dark) darkScheme.onBackground else lightScheme.onBackground
                                        )

                                        // 窗口右上角操作按钮
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(SMALL, Alignment.End),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            // 窗口是否总是在最前
                                            WindowButton(
                                                painter = painter(if (alwaysOnTop) "keep.svg" else "keep_off.svg"),
                                                description = "总在最前",
                                                isDark = dark
                                            ) {
                                                alwaysOnTop = !alwaysOnTop
                                            }

                                            // 切换暗色模式
                                            WindowButton(
                                                painter = painter(if (dark) "dark_mode.svg" else "light_mode.svg"),
                                                description = "切换暗色模式",
                                                isDark = dark
                                            ) {
                                                isDark = !dark
                                                // 将最新的暗黑模式状态写入数据库
                                                ioScope.launch {
                                                    keyValueDao.set(Key.IS_DARK_MODE, isDark!!)
                                                }
                                            }

                                            // 最小化程序
                                            WindowButton(
                                                painter = painter("minimize.svg"),
                                                description = "最小化程序",
                                                isDark = dark
                                            ) {
                                                window.isMinimized = true
                                            }

                                            // 关闭程序
                                            WindowButton(
                                                painter = painter("close.svg"),
                                                description = "关闭程序",
                                                isDark = dark
                                            ) {
                                                exitApp()
                                            }
                                        }
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(VERY_SMALL)
                                    .border(
                                        width = 1.dp,
                                        color = if (dark) darkScheme.outline else lightScheme.outline,
                                        shape = RoundedCornerShape(SMALL)
                                    )
                                    .shadow(
                                        elevation = 2.dp,
                                        shape = RoundedCornerShape(SMALL),
                                        ambientColor = Color.Black,
                                        spotColor = Color.Black
                                    )
                            ) {
                                ITodoApp { name ->
                                    println(name)
                                    screenName = name
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


/**
 * 窗口图标按钮
 * @param modifier Modifier
 * @param painter 按钮图标
 * @param description 按钮描述
 * @param isDark 是否暗黑模式
 * @param onClick 点击事件
 */
@Composable
private fun WindowButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    description: String,
    isDark: Boolean,
    onClick: () -> Unit
) {
    // 关闭程序
    IconButton(
        modifier = modifier.size(LARGE),
        onClick = {
            onClick()
        }
    ) {
        Icon(
            modifier = Modifier.size(MIDDLE),
            painter = painter,
            contentDescription = description,
            tint = if (isDark) darkScheme.onBackground else lightScheme.onBackground
        )
    }
}