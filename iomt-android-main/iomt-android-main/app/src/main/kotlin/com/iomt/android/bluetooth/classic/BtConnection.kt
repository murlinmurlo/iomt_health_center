package com.iomt.android.bluetooth.classic


import android.Manifest
import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager.AvailabilityCallback
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID
import androidx.core.app.ActivityCompat
import com.iomt.android.cardio.CardiographCommunication
import java.io.IOException


class BtConnection(cardiogaphCommunication: CardiographCommunication, context: Context, bluetoothAdapter: BluetoothAdapter) {
    var cardiogaphCommunication = cardiogaphCommunication
    private val deviceAddress = "88:6B:0F:8B:7F:29"
    private val uuid = "00001101-0000-1000-8000-00805f9b34fb"
    private val context = context
    private val bluetoothAdapter = bluetoothAdapter
    private val activity = (context as? Activity)


    companion object {
        private const val REQUEST_ENABLE_BT = 1
        private const val REQUEST_BLUETOOTH_SCAN = 2
        private const val REQUEST_ACCESS_FINE_LOCATION = 3
        private const val REQUEST_BLUETOOTH_CONNECT = 4
    }



    fun getBondedDevices(): List<BluetoothDevice> {
        val pairedDevices = mutableListOf<BluetoothDevice>()
        try {
            val bondedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
            bondedDevices?.forEach { device ->
                // Используем имя и адрес устройства
                val deviceName = device.name
                val deviceHardwareAddress = device.address
                Log.d("Bluetooth", "Устройство: $deviceName, MAC: $deviceHardwareAddress")
                pairedDevices.add(device)
            }
        } catch (e: SecurityException) {
            // Обрабатываем исключение безопасности, если разрешение не предоставлено
            Log.e("Bluetooth", "Ошибка доступа к Bluetooth-устройствам: ${e.message}")
        }
        return pairedDevices
    }

    fun startDiscoveryOfBTDevices() {
        if (activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.BLUETOOTH_CONNECT) } == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), 2)
            }
        }

        Log.d(TAG, "startDiscoveryOfBTDevices: Looking for unpaired devices.")

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // Разрешение на сканирование Bluetooth не предоставлено, запрашиваем его
            return
        }

        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
            Log.d(TAG, "startDiscoveryOfBTDevices: Canceling discovery.")
        }

        Log.d("TAG", bluetoothAdapter.state.toString())
        bluetoothAdapter.startDiscovery()
        Log.d(TAG, "ADAPTER: $bluetoothAdapter")
        Log.d(TAG, "DISCOVERY: ${bluetoothAdapter.isDiscovering}")

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
    }

    var availableDevices = mutableListOf<BluetoothDevice>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (activity != null) {
                            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ACCESS_FINE_LOCATION)
                        }
                        //return
                    }
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name ?: "Неизвестное устройство"
                    val deviceHardwareAddress = device?.address ?: "Адрес не доступен"

                    // Вывод обнаруженных устройств в логи
                    Log.d("Bluetooth", "Найдено устройство: $deviceName, MAC: $deviceHardwareAddress")

                    // Добавляем найденное устройство в список доступных
                    availableDevices.add(device!!)
                }
            }
        }
    }

    fun getAllAvailableDevices(): MutableList<BluetoothDevice> {
        Log.d(TAG, "Список: "+ availableDevices.toString())
        return availableDevices
    }


    fun BluetoothClassicConnect(deviceHardwareAddress: String) {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val device = bluetoothAdapter.getRemoteDevice(deviceHardwareAddress)

        val socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        try {
            socket.connect()
            val inputStream = socket.inputStream
            val outputStream = socket.outputStream

        } catch (e: IOException) {
            // Обработайте исключение, если не удалось подключиться
        }
    }


    fun KR2Connect(): BluetoothSocket? {
        // Device search
        if (ActivityCompat.checkSelfPermission( // вручную настроить разрешения
                context,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return null
        }
        bluetoothAdapter?.startDiscovery()
        Log.d("BluetoothClassic", "Device discovery started")

        // Connecting to the device
        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)
        val socket: BluetoothSocket? = device?.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        Thread {
            socket?.connect()
            cardiogaphCommunication.setStreams(socket?.inputStream, socket?.outputStream)
            Log.d("BluetoothClassic", "Connected to device")
        }.start()

        return socket
    }

    fun BluetoothClassicDisconnect(socket: BluetoothSocket) {
        socket?.close()
        Log.d("BluetoothClassic", "Отключение от устройства")
    }

}
