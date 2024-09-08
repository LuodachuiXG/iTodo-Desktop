package cc.loac.itodo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cc.loac.itodo.ui.ITodoApp
import cc.loac.itodo.ui.theme.*

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "iTodo",
        undecorated = true,
        transparent = true,
        state = rememberWindowState(width = 400.dp, height = 500.dp),
    ) {
        // 是否暗黑主题
        var isDark by remember {
            mutableStateOf(false)
        }

        ITodoTheme(
            darkTheme = isDark
        ) {
            Column {
                WindowDraggableArea {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(VERY_SMALL)
                            .border(
                                width = 1.dp,
                                color = if (isDark) darkScheme.outline else lightScheme.outline,
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
                                    if (isDark) darkScheme.background else lightScheme.background
                                )
                                .padding(DEFAULT_PADDING),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "iTodo",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isDark) darkScheme.onBackground else lightScheme.onBackground
                            )

                            // 窗口右上角操作按钮
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(SMALL, Alignment.End),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 切换暗色模式
                                WindowButton(
                                    imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                                    description = "切换暗色模式",
                                    isDark = isDark
                                ) {
                                    isDark = !isDark
                                }

                                // 最小化程序
                                WindowButton(
                                    imageVector = Icons.Default.Minimize,
                                    description = "最小化程序",
                                    isDark = isDark
                                ) {
                                    window.isMinimized = true
                                }

                                // 关闭程序
                                WindowButton(
                                    imageVector = Icons.Default.Close,
                                    description = "关闭程序",
                                    isDark = isDark
                                ) {
                                    exitApplication()
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
                            color = if (isDark) darkScheme.outline else lightScheme.outline,
                            shape = RoundedCornerShape(SMALL)
                        )
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(SMALL),
                            ambientColor = Color.Black,
                            spotColor = Color.Black
                        )
                ) {
                    ITodoApp()
                }
            }
        }
    }
}


/**
 * 窗口图标按钮
 * @param modifier Modifier
 * @param imageVector 按钮图标
 * @param description 按钮描述
 * @param isDark 是否暗黑模式
 * @param onClick 点击事件
 */
@Composable
private fun WindowButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
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
            imageVector = imageVector,
            contentDescription = description,
            tint = if (isDark) darkScheme.onBackground else lightScheme.onBackground
        )
    }
}