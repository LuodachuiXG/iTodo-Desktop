package cc.loac.itodo.ui

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cc.loac.itodo.ui.screens.home.HomeScreen
import cc.loac.itodo.ui.screens.login.LoginScreen
import cc.loac.itodo.ui.screens.me.MeScreen
import cc.loac.itodo.ui.theme.MIDDLE

/**
 * 路由 Bar 密封类
 * @param route 路由
 * @param name 路由名称
 * @param icon 路由图标
 */
private sealed class RouteBar(
    val route: Screens,
    val name: String,
    val icon: ImageVector
) {
    data object Me : RouteBar(Screens.ME, Screens.ME.screenName, Icons.Default.Person)
    data object Home : RouteBar(Screens.HOME, Screens.HOME.screenName, Icons.Default.Home)
    data object Login : RouteBar(Screens.LOGIN, Screens.LOGIN.screenName, Icons.Default.Lock)
}


private val bars = listOf(
    RouteBar.Me,
    RouteBar.Home,
    RouteBar.Login
)

/**
 * iTodo 程序入口
 * @param onScreenChange 屏幕切换事件（回调当前页面名）
 */
@Composable
fun ITodoApp(
    onScreenChange: (name: String) -> Unit = {}
) {
    val navController = rememberNavController()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    // 获取导航当前路由
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        bottomBar = {
            // 只有在底部导航栏的页面才显示底部导航栏
            if (currentRoute in bars.map { it.route.route }) {
                NavigationBar(
                    modifier = Modifier.height(70.dp)
                ) {
                    bars.forEach {
                        NavigationBarItem(
                            alwaysShowLabel = false,
                            selected = currentRoute == it.route.route,
                            onClick = {
                                onScreenChange(it.route.screenName)
                                navController.navigate(it.route.route) {
                                    // 清空栈内到 popUpTo ID 之间的所有 Item
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        // 保存页面状态
                                        saveState = true
                                        inclusive = true
                                    }
                                    // 避免多次点击产生多个实例
                                    launchSingleTop = true
                                    // 再次点击之前的 Item 时恢复状态
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = it.icon,
                                    contentDescription = it.name,
                                    modifier = Modifier.size(MIDDLE)
                                )
                            },
                            label = {
                                Text(it.name)
                            }
                        )
                    }
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screens.LOGIN.route,
            modifier = Modifier.padding(it).fillMaxSize(),
            enterTransition = {
                fadeIn() + scaleIn()
            },
            exitTransition = {
                fadeOut() + scaleOut()
            }
        ) {
            // 登录页面
            composable(Screens.LOGIN.route) {
                LoginScreen(navController)
            }

            // 首页
            composable(Screens.HOME.route) {
                HomeScreen(navController)
            }

            // 我
            composable(Screens.ME.route) {
                MeScreen(navController)
            }
        }
    }
}