package eu.test.healthmonitorsmartwatch

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BluetoothApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}