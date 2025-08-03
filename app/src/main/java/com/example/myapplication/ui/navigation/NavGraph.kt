package com.example.myapplication.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.R
import com.example.myapplication.ui.settings.ScreenSettings
import com.example.myapplication.ui.weather.ScreenDetails
import com.example.myapplication.ui.weather.ScreenMain
import com.example.myapplication.ui.weather.WeatherViewModel

sealed class Destinations(val route: String, val labelRes: Int, val iconRes: Int) {
    data object Main : Destinations("inicio", R.string.tab_inicio, android.R.drawable.ic_menu_myplaces)
    data object Details : Destinations("detalles", R.string.tab_detalles, android.R.drawable.ic_menu_info_details)
    data object Settings : Destinations("ajustes", R.string.tab_ajustes, android.R.drawable.ic_menu_preferences)
}

@Composable
fun WeatherNavHost() {
    val navController = rememberNavController()
    val items = listOf(Destinations.Main, Destinations.Details, Destinations.Settings)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { dest ->
                    NavigationBarItem(
                        icon = { Icon(painterResource(id = dest.iconRes), contentDescription = null) },
                        label = { Text(text = stringResource(id = dest.labelRes)) },
                        selected = currentDestination.isRouteInHierarchy(dest.route),
                        onClick = {
                            if (!currentDestination.isRouteInHierarchy(dest.route)) {
                                navController.navigate(dest.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destinations.Main.route,
            modifier = androidx.compose.ui.Modifier.padding(innerPadding)
        ) {
            // Register all routes directly; avoid touching navController.graph before NavHost sets it
            composable(Destinations.Main.route) {
                val vm: WeatherViewModel = hiltViewModel()
                ScreenMain(vm)
            }
            composable(Destinations.Details.route) {
                val vm: WeatherViewModel = hiltViewModel()
                ScreenDetails(vm)
            }
            composable(Destinations.Settings.route) {
                ScreenSettings()
            }
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: String): Boolean {
    return this?.hierarchy?.any { it.route == route } == true
}

/* removed addMain helper to prevent accessing navController.graph prematurely */
