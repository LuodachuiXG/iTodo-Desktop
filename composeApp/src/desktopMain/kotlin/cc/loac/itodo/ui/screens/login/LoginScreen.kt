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
import org.koin.compose.viewmodel.koinNavViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

/**
 * 登录 Screen
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
fun LoginScreen(
    navController: NavHostController,
    vm: LoginViewModel = koinNavViewModel()
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