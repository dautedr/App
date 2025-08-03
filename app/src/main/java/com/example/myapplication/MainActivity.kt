package com.example.myapplication

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.ui.navigation.WeatherNavHost
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.activity.compose.rememberLauncherForActivityResult
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.weather.WeatherScreen
import com.example.myapplication.ui.navigation.Destinations
import com.example.myapplication.ui.weather.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val locationPermissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions: Map<String, Boolean> ->
                    if (permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                        permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)) {
                        Log.d("MainActivity", "Location permission granted")
                        viewModel.fetchWeatherForCurrentLocation()
                    } else {
                        Log.d("MainActivity", "Location permission denied")
                        viewModel.fetchWeather("Las Palmas de Gran Canaria")
                    }
                }

                LaunchedEffect(Unit) {
                    locationPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }

                // Replace single screen with NavHost-based app entry
                WeatherNavHost()
            }
        }
    }
}
