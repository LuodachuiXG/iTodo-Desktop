package cc.loac.itodo.ui.screens.home

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
 * 首页 Screen
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    vm: HomeViewModel = viewModel { HomeViewModel() }
) {
    Column(
        modifier = Modifier.padding(DEFAULT_PADDING)
    ) {
        Text(
            text = "首页",
            style = MaterialTheme.typography.titleLarge
        )
    }
}