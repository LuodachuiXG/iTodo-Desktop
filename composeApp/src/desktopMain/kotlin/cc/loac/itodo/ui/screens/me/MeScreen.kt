package cc.loac.itodo.ui.screens.me

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cc.loac.itodo.data.models.enums.ScreenWidth
import cc.loac.itodo.ui.Screens
import cc.loac.itodo.ui.theme.DEFAULT_PADDING
import cc.loac.itodo.ui.theme.DEFAULT_SPACING
import cc.loac.itodo.ui.theme.SMALL
import cc.loac.itodo.ui.theme.VERY_SMALL
import cc.loac.itodo.util.painter
import org.koin.compose.viewmodel.koinViewModel

/**
 * 我 Screen
 * @param navController 导航控制器
 * @param snackBar SnackBar控制器
 * @param screenWidth 屏幕宽度枚举
 * @param vm ViewModel
 */
@Composable
fun MeScreen(
    navController: NavHostController,
    snackBar: SnackbarHostState,
    screenWidth: ScreenWidth,
    vm: MeViewModel = koinViewModel()
) {
    Column(
        modifier = Modifier.padding(DEFAULT_PADDING)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(
                when (screenWidth) {
                    ScreenWidth.NORMAL -> 1
                    ScreenWidth.WIDE -> 2
                    ScreenWidth.EXTRA_WIDE -> 3
                    ScreenWidth.ULTRA_WIDE -> 4
                }
            ),
            modifier = Modifier.clip(RoundedCornerShape(VERY_SMALL)),
            verticalArrangement = Arrangement.spacedBy(SMALL),
            horizontalArrangement = Arrangement.spacedBy(DEFAULT_PADDING, Alignment.Start)
        ) {
            item {
                ItemCard(
                    icon = painter("theme.svg"),
                    text = "主题设置"
                ) {
                    // 跳转主题页面
                    navController.navigate(Screens.THEME.route)
                }
            }
        }
    }
}

/**
 * 项目卡片
 * @param icon 图标
 * @param text 文本
 * @param onClick 点击事件
 */
@Composable
private fun ItemCard(
    icon: Painter,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(CardDefaults.shape)
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier.padding(DEFAULT_PADDING),
            horizontalArrangement = Arrangement.spacedBy(DEFAULT_SPACING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = text
            )

            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
    }
}