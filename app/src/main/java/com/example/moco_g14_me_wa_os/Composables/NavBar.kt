package com.example.moco_g14_me_wa_os.Composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.NavigationBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.moco_g14_me_wa_os.Settings.SettingScreen

data class NavItem(
    val title : String,
    val selectedOption : ImageVector,
    val unselectedOption: ImageVector
)

@Preview
@Composable
fun NavigationBar(state : Int, navController: NavController) {
    val screens = (SettingScreen())
    val navOptions = listOf(
        NavItem(
            title = "To-Do`s",
            selectedOption = Icons.Filled.CheckCircle,
            unselectedOption = Icons.Outlined.CheckCircle
        ),
        NavItem(
            title = "Timer",
            selectedOption = Icons.Filled.CheckCircle,
            unselectedOption = Icons.Outlined.CheckCircle
        ),
        NavItem(
            title = "Settings",
            selectedOption = Icons.Filled.CheckCircle,
            unselectedOption = Icons.Outlined.CheckCircle
        )
    )
    var selectedIndex by rememberSaveable { mutableStateOf(state) }

    NavigationBar {
        navOptions.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    navController.navigate(screens[index])},
                label = {
                    Text(text = item.title)
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedIndex) {
                            item.selectedOption
                        } else item.unselectedOption,
                        contentDescription = item.title
                    )

                }
            )
        }
    }
}
