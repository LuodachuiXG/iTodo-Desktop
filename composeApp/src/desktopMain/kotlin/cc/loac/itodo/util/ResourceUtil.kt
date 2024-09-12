package cc.loac.itodo.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource

/**
 * 加载 resources/drawable 下图片资源
 * @param fileName 图片文件名
 */
@Composable
fun painter(fileName: String): Painter {
    return painterResource("drawable/$fileName")
}

