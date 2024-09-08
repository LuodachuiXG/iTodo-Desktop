package cc.loac.itodo.ui.screens.me

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
 * 我 Screen
 */
@Composable
fun MeScreen(
    navController: NavHostController,
    vm: MeViewModel = viewModel { MeViewModel() }
) {
    Column(
        modifier = Modifier.padding(DEFAULT_PADDING)
    ) {
        Text(
            text = "我",
            style = MaterialTheme.typography.titleLarge
        )
    }
}