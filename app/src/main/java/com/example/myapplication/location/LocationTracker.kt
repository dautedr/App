package com.example.myapplication.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class LocationTracker @Inject constructor(
    private val application: Application,
    // Remove DI for client; we'll create it from context to avoid missing tasks.await dependency wiring issues
    private val locationClient: FusedLocationProviderClient
) {
    suspend fun getCurrentLocation(): Location? {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasAccessFineLocationPermission && !hasAccessCoarseLocationPermission) {
            return null
        }

        return try {
            // Use the lastLocation Task and await manually without kotlinx-coroutines-play-services
            val task = locationClient.lastLocation
            val location = com.google.android.gms.tasks.Tasks.await(task)
            location
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
}
