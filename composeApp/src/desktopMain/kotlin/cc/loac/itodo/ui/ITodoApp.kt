package cc.loac.itodo.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cc.loac.itodo.data.flows.AlertFlow
import cc.loac.itodo.data.models.AlertFlowModel
import cc.loac.itodo.data.models.enums.ScreenWidth
import cc.loac.itodo.ui.components.MyAlertDialog
import cc.loac.itodo.ui.screens.home.HomeScreen
import cc.loac.itodo.ui.screens.login.LoginScreen
import cc.loac.itodo.ui.screens.me.MeScreen
import cc.loac.itodo.ui.screens.theme.ThemeScreen
import cc.loac.itodo.ui.theme.DEFAULT_PADDING
import cc.loac.itodo.ui.theme.MIDDLE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
}

// 底部显示的 Bar
private val bars = listOf(
    RouteBar.Home,
    RouteBar.Me
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

    // 当前屏幕宽度 Px
    var currentWidth by remember {
        mutableIntStateOf(0)
    }
    // 当前屏幕宽度 Dp
    val currentWidthDp = with(LocalDensity.current) {
        currentWidth.toDp()
    }

    // 当前屏幕宽度枚举
    val screenWidth = ScreenWidth.of(currentWidthDp)

    // 获取导航当前路由
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val scope = rememberCoroutineScope()


    // 当前是否正在显示 Alert
    var showAlert by remember {
        mutableStateOf(false)
    }

    // 当前显示的 Alert 的配置信息
    var alertFlowModel by remember {
        mutableStateOf(AlertFlowModel())
    }

    MyAlertDialog(
        visible = showAlert,
        alertFlowModel = alertFlowModel,
        onDismiss = {
            showAlert = false
            alertFlowModel = AlertFlowModel()
        }
    )

    // 监听 Alert 消息流，用于显示弹窗
    scope.launch {
        AlertFlow.flow.collect {
            // 如果当前有弹窗正在显示，循环等待
            if (showAlert) {
                alertFlowModel = AlertFlowModel()
                showAlert = false
            }

            alertFlowModel = it
            showAlert = true
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        bottomBar = {
            // 只有在底部导航栏的页面才显示底部导航栏
            AnimatedVisibility(
                visible = currentRoute in bars.map { it.route.route } && screenWidth == ScreenWidth.NORMAL
            ) {
                NavigationBar(
                    modifier = Modifier.height(70.dp)
                ) {
                    bars.forEach {
                        NavigationBarItem(
                            alwaysShowLabel = false,
                            selected = currentRoute == it.route.route,
                            onClick = {
                                if (currentRoute != it.route.route) {
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
        Row(
            modifier = Modifier.onGloballyPositioned {
                currentWidth = it.size.width
            }
        ) {
            AnimatedVisibility(
                visible = currentRoute in bars.map { it.route.route } && screenWidth != ScreenWidth.NORMAL
            ) {
                NavigationRail(
                    modifier = Modifier.width(80.dp).padding(top = DEFAULT_PADDING),
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    bars.forEach {
                        NavigationRailItem(
                            alwaysShowLabel = true,
                            selected = currentRoute == it.route.route,
                            onClick = {
                                if (currentRoute != it.route.route) {
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


            // 导航 Navigation
            NavHost(
                navController = navController,
                startDestination = Screens.LOGIN.route,
                modifier = Modifier.padding(it).fillMaxSize(),
                enterTransition = {
                    fadeIn() + slideInHorizontally()
                },
                exitTransition = {
                    fadeOut() + slideOutHorizontally(targetOffsetX = { it / 2 })
                }
            ) {
                // 登录页面
                composable(Screens.LOGIN.route) {
                    LoginScreen(navController, snackBarHostState)
                }

                // 首页
                composable(Screens.HOME.route) {
                    HomeScreen(navController, snackBarHostState, screenWidth)
                }

                // 我
                composable(Screens.ME.route) {
                    MeScreen(navController, snackBarHostState, screenWidth)
                }

                // 主题
                composable(Screens.THEME.route) {
                    ThemeScreen(navController, snackBarHostState)
                }
            }
        }
    }
}