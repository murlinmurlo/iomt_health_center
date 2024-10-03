package com.iomt.android.compose.view.bt

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.iomt.android.bluetooth.classic.BtConnection
import com.iomt.android.cardio.CardiographCommunication
import com.iomt.android.cardio.FileHandler


@Composable
fun BtScannerView() {
    val fileName = "ecg.txt"
    val timestamps = "timestamps.txt"
    val export = "export.txt"
    val ecgChannel_1 = "channel_1.txt"
    val ecgChannel_2 = "channel_2.txt"
    val timestampEcg = "timestamp_ecg.txt"

    val context = LocalContext.current
    val bluetoothManager: BluetoothManager = (context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)!!
    val bluetoothAdapter = bluetoothManager.adapter
    val fileHandler = FileHandler(context, "ecg.txt", "channel_1.txt", "channel_2.txt")
    val cardiographCommunication = CardiographCommunication(context, fileHandler)
    val btConnection = BtConnection(cardiographCommunication, context, bluetoothAdapter)

    val (pairedDevices, availableDevices, refreshDevices) = rememberBluetoothDeviceState(btConnection)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp), // Отступ сверху
            contentAlignment = Alignment.TopCenter // Выравнивание по центру сверху
        ) {
            Button(
                onClick = {
                    btConnection.startDiscoveryOfBTDevices()
                    refreshDevices()
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White // Белый цвет текста
                )
            ) {
                Text(text = "Refresh")
            }
        }

        BluetoothDevicesList(
            pairedDevices = pairedDevices,
            availableDevices = availableDevices,
            onDeviceClicked = { device ->
                btConnection.BluetoothClassicConnect(device.address)
            }
        )
    }
}


@Composable
fun rememberBluetoothDeviceState(
    btConnection: BtConnection
): Triple<List<BluetoothDevice>, MutableList<BluetoothDevice>, () -> Unit> {
    val pairedDevices = remember { mutableStateListOf<BluetoothDevice>() }
    val availableDevices = remember { mutableStateListOf<BluetoothDevice>() }

    val refreshDevices: () -> Unit = {
        pairedDevices.clear()
        availableDevices.clear()
        pairedDevices.addAll(btConnection.getBondedDevices())
        availableDevices.addAll(btConnection.getAllAvailableDevices())
    }

    LaunchedEffect(Unit) {
        refreshDevices()
    }

    return Triple(pairedDevices, availableDevices, refreshDevices)
}


@Composable
fun BluetoothDevicesList(
    pairedDevices: List<BluetoothDevice>,
    availableDevices: MutableList<BluetoothDevice>,
    onDeviceClicked: (BluetoothDevice) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Paired devices",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(pairedDevices) { device ->
                BluetoothDeviceItem(
                    device = device,
                    onDeviceClicked = onDeviceClicked
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Available devices",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(availableDevices) { device ->
                BluetoothDeviceItem(
                    device = device,
                    onDeviceClicked = onDeviceClicked
                )
            }
        }
    }
}


@Composable
fun BluetoothDeviceItem(
    device: BluetoothDevice,
    onDeviceClicked: (BluetoothDevice) -> Unit
) {
    val context = LocalContext.current

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Разрешение на подключение к Bluetooth-устройствам не предоставлено
        return
    }

    Card(
        modifier = Modifier
            .fillMaxWidth() // Возвращаем ширину к максимальной
            .clickable { onDeviceClicked(device) }
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(device.name ?: "Неизвестное устройство")
            Text(device.address)
        }
    }
}





/*
fun connectToDevice(device: BluetoothDevice) {
    // Реализация подключения к выбранному устройству
    val socket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
    socket.connect()
    // Установка потоков ввода/вывода
    cardiographCommunication.setStreams(socket.inputStream, socket.outputStream)
    Log.d("BluetoothClassic", "Подключение к устройству установлено")
}
*/





