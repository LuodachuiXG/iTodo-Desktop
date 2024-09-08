package cc.loac.itodo.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import cc.loac.itodo.ui.theme.DEFAULT_PADDING

/**
 * 登录 Screen
 */
@Composable
fun LoginScreen(
    navController: NavHostController,
    vm: LoginViewModel = viewModel { LoginViewModel() }
) {
    Column(
        modifier = Modifier.padding(DEFAULT_PADDING)
    ) {
        Text(
            text = "登录",
            style = MaterialTheme.typography.titleLarge
        )
    }
}