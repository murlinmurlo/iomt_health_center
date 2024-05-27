package com.iomt.android.compose.view.bt


import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.iomt.android.bluetooth.classic.BtConnection
import com.iomt.android.cardio.CardiographCommunication
import com.iomt.android.cardio.FileHandler
import com.iomt.android.mqtt.BcTopic
import com.iomt.android.mqtt.MqttClient
import com.iomt.android.mqtt.MqttRecordMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.toLocalDateTime
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID


@Preview
@Composable
fun CardioView() {
    val context = LocalContext.current
    val bluetoothManager: BluetoothManager = (context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)!!
    val bluetoothAdapter = bluetoothManager.adapter
    val fileHandler = FileHandler(context, "ecg.txt", "channel_1.txt", "channel_2.txt")
    val cardiographCommunication = CardiographCommunication(context, fileHandler)
    val btConnection = BtConnection(cardiographCommunication, context, bluetoothAdapter)

    //var ecgData by remember { mutableStateOf(listOf<Float>()) }
    val ecgDataState = remember { mutableStateOf(listOf<Float>(0.0f)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp)
                .width(150.dp)
        ) {
            var bluetoothSocket: BluetoothSocket? by remember { mutableStateOf(null) }
            var isConnected by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    if (!isConnected) {
                        bluetoothSocket = BluetoothClassicConnect(context, cardiographCommunication)
                        isConnected = true
                    } else {
                        if (bluetoothSocket != null) {
                            BluetoothClassicDisconnect(bluetoothSocket!!)
                            isConnected = false
                        }
                    }
                },
                modifier = Modifier
                    .width(150.dp)
            ) {
                Text(text = if (isConnected) "Disconnect" else "Connect")
            }

            Spacer(modifier = Modifier.width(8.dp))

            var isVisible by remember { mutableStateOf(true) }
            Button(
                onClick = {
                    cardiographCommunication.PowerOff()
                    isVisible = false
                },
                modifier = Modifier
                    .scale(if (isVisible) 1f else 0f)
                    .width(150.dp)
            ) {
                Text("Turn off")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        EcgGraph(ecgDataState.value)

        Spacer(modifier = Modifier.height(3.dp))

        var frequency = FrequencySelector()

        Spacer(modifier = Modifier.height(3.dp))

        var isMonitoring by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier
                    .size(width = 100.dp, height = 100.dp)
                    .padding(8.dp),
                shape = CircleShape,
                onClick = {
                    isMonitoring = true
                    cardiographCommunication.MonitoringMode(frequency) { decodedData -> }
                }
            ) {
                Text("Start")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                modifier = Modifier
                    .size(width = 100.dp, height = 100.dp)
                    .padding(8.dp),
                shape = CircleShape,
                onClick = {
                    cardiographCommunication.MonitoringMode(0x00) { decodedData ->
                    }

                    val (ecg_graph_1, ecg_graph_2) = fileHandler.readPartial_Float("ecg.txt", frequency)
                    ecgDataState.value =  ecg_graph_1
                }
            ) {
                Text("Stop")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        SendDataButton(
            context = context,
            cardiographCommunication = cardiographCommunication,
            fileHandler = fileHandler,
            frequency = frequency
        )
    }
}



@Composable
fun FrequencySelector(): Int {
    var frequency by remember { mutableStateOf(0x01) }
    val values = listOf("250 Hz", "500 Hz", "1000 Hz")
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf(values[0]) }

    Column(modifier = Modifier.padding(16.dp)) {
        Button(
            onClick = { expanded = true },
            modifier = Modifier
                .size(width = 120.dp, height = 50.dp) // Устанавливаем размер кнопки
        ) {
            Text(text = selectedLabel)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            values.forEachIndexed { index, label ->
                DropdownMenuItem(
                    onClick = {
                        selectedLabel = label
                        frequency = when (index) {
                            0 -> 0x01
                            1 -> 0x02
                            2 -> 0x03
                            else -> frequency
                        }
                        expanded = false
                    }
                ) {
                    Text(text = label)
                }
            }
        }
    }
    return frequency
}


@Composable
fun SendDataButton(context: Context, cardiographCommunication: CardiographCommunication, fileHandler: FileHandler, frequency: Int) {
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            coroutineScope.launch {
                val result = sendData(context, cardiographCommunication, fileHandler, frequency)
                Toast.makeText(context, result, Toast.LENGTH_LONG).show()
            }
        },
        modifier = Modifier
            .size(width = 120.dp, height = 50.dp)
    ) {
        Text("Send")
    }

}


suspend fun sendData(context: Context, cardiographCommunication: CardiographCommunication, fileHandler: FileHandler, freq: Int): String {

    val frequency = when(freq){
        1 -> 250
        2 -> 500
        3 -> 1000
        else -> {Log.d(TAG, "Частота не определена")}
    }
    Log.d("ЧАСТОТА", frequency.toString())
    return try {
        withContext(Dispatchers.IO) {
            val timeArray = cardiographCommunication.GetTimeArray()
            val file = fileHandler.readFile("ecg.txt")
            Log.d("В ФАЙЛЕ С ЭКГ ", file)

            val ecg_data = fileHandler.readPartial("ecg.txt", frequency)
            val (ecgChannel1, ecgChannel2) = ecg_data

            Log.d(ContentValues.TAG, "ecg_channel_1.size: ${ecgChannel1.size}, ecg_channel_2.size: ${ecgChannel1.size}")

            val minSize = minOf(timeArray.size, ecgChannel1.size, ecgChannel2.size)

            val data = (0 until minSize).map { index ->
                val timestamp = timeArray[index].toLocalDateTime()
                val value = "[${ecgChannel1[index]},${ecgChannel2[index]}]"
                Log.d("СООБЩЕНИЕ", "time: ${timestamp}, val: ${value}")

                MqttRecordMessage(value, timestamp)
            }

            val mqttClient = MqttClient()
            mqttClient.connect()
            data.forEach { message ->
                val topic = BcTopic(userId = 12345L, deviceMac = "88:6B:0F:8B:7F:29", frequency)
                mqttClient.bcsend(topic, message)
            }
            mqttClient.disconnect()
        }
        "Данные успешно отправлены"
    } catch (e: Exception) {
        e.printStackTrace()
        "Ошибка при отправке данных: ${e.message}"
    }
}


private fun BluetoothClassicConnect(context: Context, cardiographCommunication: CardiographCommunication): BluetoothSocket? {
    val deviceAddress = "88:6B:0F:8B:7F:29" //КР-2
    //private val deviceAddress = "18:26:54:F1:17:E2" //Машин смартфон
    val uuid = "00001101-0000-1000-8000-00805f9b34fb"
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    // Checking Bluetooth availability
    //val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    if (bluetoothAdapter == null) {
        // The device does not support Bluetooth
        Log.d("BluetoothClassic", "Device does not support Bluetooth")
    }

    // Device search
    if (ActivityCompat.checkSelfPermission( // вручную настроить разрешения
            context,
            Manifest.permission.BLUETOOTH_SCAN
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return null
    }
    bluetoothAdapter?.startDiscovery()
    Log.d("BluetoothClassic", "Device discovery started")

    // Connecting to the device
    val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)
    Log.d("BluetoothClassic", device.toString())
    val socket: BluetoothSocket? = device?.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
    Thread {
        socket?.connect()
        cardiographCommunication.setStreams(socket?.inputStream, socket?.outputStream)
        Log.d("BluetoothClassic", "Connected to device")
    }.start()

    return socket
}

private fun BluetoothClassicDisconnect(socket: BluetoothSocket) {
    var inputStream: InputStream? = null
    var outputStream: OutputStream? = null
    // Закрыть Bluetooth-сокет и освободить ресурсы
    inputStream?.close()
    outputStream?.close()
    socket?.close()
    Log.d("BluetoothClassic", "Отключение от устройства")
}



