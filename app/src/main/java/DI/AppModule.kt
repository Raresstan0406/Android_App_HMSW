package DI

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import data.AndroidBluetoothController
import data.TempPluseSpO2RecieveManager
import data.ble.TempPulseSpO2BLERecieveManager
import domain.bluetooth.BluetoothController
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothController {
        return AndroidBluetoothController(context)
    }

    @Provides
    @Singleton
    fun provideBluetoothAdapter(@ApplicationContext context: Context): BluetoothAdapter {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return manager.adapter
    }

    @Provides
    @Singleton
    fun provideTempPulseSpO2ReceiveManager(
        @ApplicationContext context: Context,
        bluetoothAdapter: BluetoothAdapter
    ): TempPluseSpO2RecieveManager {
        return TempPulseSpO2BLERecieveManager(bluetoothAdapter,context)
    }
}