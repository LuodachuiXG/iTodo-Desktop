package cc.loac.itodo.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

/**
 * 加载 commonMain/composeResources/drawable 下图片资源
 * @param fileName 图片文件名
 */
@Composable
fun painter(fileName: String) =
    painterResource("composeResources/itodo_desktop.composeapp.generated.resources/drawable/$fileName")