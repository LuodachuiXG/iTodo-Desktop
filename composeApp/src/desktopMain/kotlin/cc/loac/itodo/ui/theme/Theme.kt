package cc.loac.itodo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import kotlinx.coroutines.flow.MutableStateFlow

object Theme {
    // 主题颜色流，修改主题颜色给此流 emit 种子颜色即可
    val themeFlow = MutableStateFlow<Color?>(null)
}

/**
 * iTodo 主题
 * @param color 主题种子颜色
 * @param darkTheme 是否为暗黑主题
 * @param content Compose 内容
 */
@Composable
fun ITodoTheme(
    color: Color? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    var seedColor by remember {
        mutableStateOf(color ?: Color(0xFF63A002))
    }

    LaunchedEffect(color) {
        color?.let {
            seedColor = it
        }
    }

    LaunchedEffect(Unit) {
        Theme.themeFlow.collect {
            it?.let {
                seedColor = it
            }
        }
    }

    DynamicMaterialTheme(
        seedColor = seedColor,
        useDarkTheme = darkTheme,
        content = content
    )
}
