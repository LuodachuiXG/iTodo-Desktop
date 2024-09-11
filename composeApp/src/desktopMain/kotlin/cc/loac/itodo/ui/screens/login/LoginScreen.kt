package cc.loac.itodo.ui.screens.login

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavHostController
import cc.loac.itodo.data.sql.dao.TodoDao
import cc.loac.itodo.ui.Screens
import cc.loac.itodo.ui.theme.DEFAULT_PADDING
import cc.loac.itodo.ui.theme.SMALL
import cc.loac.itodo.ui.theme.VERY_SMALL
import cc.loac.itodo.util.canClick
import cc.loac.itodo.util.painter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinNavViewModel
import org.koin.core.annotation.KoinExperimentalAPI

/**
 * 登录 Screen
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    snackBar: SnackbarHostState,
    vm: LoginViewModel = koinNavViewModel()
) {
    val scope = rememberCoroutineScope()

    // iTodo 站点地址
    var url by remember {
        mutableStateOf("")
    }

    // 用户名
    var username by remember {
        mutableStateOf("")
    }

    // 密码
    var password by remember {
        mutableStateOf("")
    }

    Column {
        Title()
        LoginContainer(
            url = url,
            username = username,
            password = password,
            onUrlChanged = { url = it },
            onUsernameChanged = { username = it },
            onPasswordChanged = { password = it },
            onLocalUseClick = {
                // 本地使用点击回调
                canClick {
                    scope.launch {
                        val result = snackBar.showSnackbar("后期也可以同步到服务器", "使用本地存储", true)
                        if (result == SnackbarResult.ActionPerformed) {
                            // 确定
                            navController.navigate(Screens.HOME.route)
                        }
                    }
                }
            }
        ) {
            // 登录按钮点击回调
        }
    }
}

/**
 * 标题
 */
@Composable
private fun Title() {
    var showITodo by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        // 延迟 50 毫秒后显示 iTodo
        delay(50)
        showITodo = true
    }

    Card(
        modifier = Modifier.padding(DEFAULT_PADDING)
    ) {
        Row(
            modifier = Modifier
                .padding(DEFAULT_PADDING)
                .animateContentSize()
        ) {
            Text(
                text = "登录",
                style = MaterialTheme.typography.titleLarge
            )
            if (showITodo) {
                Text(
                    text = "iTodo",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = SMALL),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/**
 * 登录容器
 * @param url iTodo 服务器地址
 * @param username 用户名
 * @param password 密码
 * @param onUrlChanged iTodo 服务器地址改变回调
 * @param onUsernameChanged 用户名改变回调
 * @param onPasswordChanged 密码改变回调
 * @param onLocalUseClick 本地使用按钮点击回调
 * @param onLoginClicked 登录按钮点击回调
 */
@Composable
private fun LoginContainer(
    url: String,
    username: String,
    password: String,
    onUrlChanged: (String) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLocalUseClick: () -> Unit,
    onLoginClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = DEFAULT_PADDING)
            .fillMaxWidth()
    ) {
        Input(
            value = url,
            onValueChanged = onUrlChanged,
            label = "服务器地址",
            placeholder = "请输入 iTodo 服务器地址"
        )

        Input(
            value = username,
            onValueChanged = onUsernameChanged,
            label = "用户名",
            placeholder = "请输入用户名"
        )

        Input(
            value = password,
            onValueChanged = onPasswordChanged,
            label = "密码",
            placeholder = "请输入密码",
            isPassword = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(SMALL, Alignment.End)
        ) {
            Button(
                onClick = {
                    onLoginClicked()
                }
            ) {
                Text(text = "登录")
            }

            OutlinedButton(
                onClick = {
                    onLocalUseClick()
                }
            ) {
                Text(text = "本地使用")
            }
        }
    }
}

/**
 * 输入框封装
 */
@Composable
private fun Input(
    value: String,
    onValueChanged: (String) -> Unit,
    label: String,
    placeholder: String? = null,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = {
            Text(label)
        },
        placeholder = if (placeholder.isNullOrBlank()) null else {
            {
                Text(placeholder)
            }
        },
        trailingIcon = if (value.isBlank()) null else {
            {
                IconButton(
                    onClick = { onValueChanged("") },
                    modifier = Modifier.pointerHoverIcon(
                        icon = PointerIcon.Hand
                    )
                ) {
                    Icon(
                        painter = painter("close.svg"),
                        contentDescription = "清除数据"
                    )
                }
            }
        },
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        modifier = Modifier
            .padding(bottom = VERY_SMALL)
            .fillMaxWidth()
    )
}
