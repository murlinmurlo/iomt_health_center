package com.my.test

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.nfc.Tag
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.my.test.databinding.ActivityMainBinding
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.util.Arrays
import java.util.UUID


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val deviceAddress = "88:6B:0F:8B:7F:29" //КР-2
    //private val deviceAddress = "18:26:54:F1:17:E2" //Машин смартфон
    private val uuid = "00001101-0000-1000-8000-00805f9b34fb"
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var fileExist = false
    private val fileName = "ecg.txt"
    private val timestamps = "timestamps.txt"
    private val export = "export.txt"
    private val ecgChannel_1 = "channel_1.txt"
    private val ecgChannel_2 = "channel_2.txt"
    private val timestampEcg = "timestamp_ecg.txt"




    private lateinit var cardiogaphCommunication: CardiogaphCommunication

    companion object {
        private const val REQUEST_ENABLE_BT = 1
        private const val REQUEST_BLUETOOTH_CONNECT = 2
        private const val REQUEST_BLUETOOTH_SCAN = 3
        private const val REQUEST_ACCESS_FINE_LOCATION = 4
    }


    //toolbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cardiograph_menu, menu)
        return true
    }

    //toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var battaryCharge = cardiogaphCommunication.ReadComponentStatus()?.get(3).toString()

        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.battary -> {
                Toast.makeText(this, "${battaryCharge}%", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val fileHandler = FileHandler(this, fileName, ecgChannel_1, ecgChannel_1, timestampEcg)
        val ecgView: EcgShowView = findViewById(R.id.ecgShowView)
        cardiogaphCommunication = CardiogaphCommunication(this, fileHandler, timestamps, export)


        //меню toolbar
        supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.lightYellow)))
            setDisplayHomeAsUpEnabled(true)
            title = "Cardio"
        }

        //кнопка подключения к KR-2
        var flag = true
        var bluetoothSocket: BluetoothSocket? = null
        binding.onClickConnect.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // Запрашиваем разрешение у пользователя
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_BLUETOOTH_CONNECT)
            } else {
                // Разрешение предоставлено, получаем список сопряженных устройств
                getBondedDevices()
            }

            if (flag) {
                // При нажатии на кнопку "Connect"
                bluetoothSocket = BluetoothClassicConnect()
                flag = false
                binding.onClickConnect.text = "Disconnect"
            } else {
                // При нажатии на кнопку "Disconnect"
                if (bluetoothSocket != null) {
                    BluetoothClassicDisconnect(bluetoothSocket!!)
                    binding.onClickConnect.text = "Connect"
                }
                flag = true
            }
        }



        var frequency = 0x01
        val values = arrayOf("250 Hz", "500 Hz", "1000 Hz")
        val spinner = binding.selectFrequency
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, values)
        spinner.adapter = adapter
        spinner.setPrompt("Select frequency") // Устанавливаем текст для выпадающего списка
        //spinner.setSelection(0)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (position) {
                    0 -> frequency = 0x01
                    1 -> frequency = 0x02
                    2 -> frequency = 0x03
                }
            }
        }



        binding.onClickReadDeviceInformation.setOnClickListener {
            val result = cardiogaphCommunication.ReadDeviceInformation(this)
            val decodedResult = result.contentToString()
            binding.showDeviceInformation.text = decodedResult
            binding.showDeviceInformation.visibility = View.VISIBLE
        }


        //кнопка выключения
        binding.onClickPowerOff.setOnClickListener {
            cardiogaphCommunication.PowerOff()
            binding.onClickPowerOff.visibility = View.GONE
        }

        fun checkIfFileExists(): Boolean {
            return try {
                val file = File(filesDir, fileName)
                file.exists()
            } catch (e: Exception) {
                Log.e("BT", "Error checking file existence: $e")
                false
            }
        }


        findViewById<Button>(R.id.onClickShare).setOnClickListener { view ->
            fun onClickShareBC() {
                val time_mas = cardiogaphCommunication.GetTimeArray()
                val ecg_mas = fileHandler.readPartial(frequency)
                val (ecg_channel_1, ecg_channel_2) = ecg_mas

                Log.d(TAG, "time_mas.size: ${time_mas.size}, ecg_channel_1.size: ${ecg_channel_1.size}, ecg_channel_2.size: ${ecg_channel_2.size}")

                val minSize = minOf(time_mas.size, ecg_channel_1.size, ecg_channel_2.size)

                val ecgExportFile = "ecg_export.txt"
                val fileOutputStream = openFileOutput(ecgExportFile, Context.MODE_PRIVATE)
                val outputWriter = OutputStreamWriter(fileOutputStream)

                for (i in 0 until minSize) {
                    outputWriter.write("${time_mas[i]} [${ecg_channel_1[i]},${ecg_channel_2[i]}]\n")
                }

                outputWriter.flush()
                outputWriter.close()

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/*"
                val file1 = getFileStreamPath(ecgExportFile)
                val fileUri = FileProvider.getUriForFile(
                    this@MainActivity,
                    "com.my.test.provider",
                    file1,
                )
                intent.putExtra(Intent.EXTRA_STREAM, fileUri)
                startActivity(Intent.createChooser(intent, "share file with"))

            }
            onClickShareBC()
        }


        //кнопка включения мониторинга ЭКГ
        //var isMonitoring = false
        binding.onClickStartSession.setOnClickListener {
            cardiogaphCommunication.MonitoringMode(frequency) { decodedData ->
            }
        }

        //кнопка включения мониторинга ЭКГ
        //var isMonitoring = false
        binding.onClickStopSession.setOnClickListener {
            cardiogaphCommunication.MonitoringMode(0x00) { decodedData ->
            }

        }


        binding.onClickShow.setOnClickListener {

            val time_mas = cardiogaphCommunication.GetTimeArray()
            val ecg_mas = fileHandler.readPartial(frequency)
            val (ecg_channel_1, ecg_channel_2) = ecg_mas
            val ecg_str_1 = ecg_channel_1.toString()
                .removeSurrounding("[", "]")
                .replace("][", ", ")
            val ecg_str_2 = ecg_channel_2.toString()
                .removeSurrounding("[", "]")
                .replace("][", ", ")


            Log.d("tag", "Прочитанный текст: $ecg_str_1")

            val ecgShowView = binding.ecgShowView
            ecgShowView.setData(ecg_str_1)



//            val textToWrite = fileHandler.readFile("ecg.txt")
//            fileHandler.writeFile(textToWrite)
//
//            fileHandler.readPartial(frequency)
//
//            val channel_1 = fileHandler.readFile("channel_1.txt")
//            //fileHandler.writeFile("", "channel_1.txt", false)
//            Log.d("tag", "Прочитанный текст: $channel_1")
//
//            val channal_1_int24 = channel_1.trim().split("\\s+".toRegex()).map { it.toInt(16)}.toString()
//            Log.d("tag", "Прочитанный текст: $channal_1_int24")
//
//            val channel_1_float = fileHandler.convertAndDivideToString(channal_1_int24)
//            Log.d("tag", "Прочитанный текст: $channel_1_float")
//
//            val ecgShowView = binding.ecgShowView
//            ecgShowView.setData(channel_1_float)
//
//            val time_stamps = fileHandler.readAndClearFile("timestamps.txt")
//            Log.d("tag", "Отметки: $time_stamps")
//
//
//            fileHandler.writeFile("", fileName, false)
//            fileHandler.writeFile("", export, false)
        }


        //тестовая кнопка button
        binding.button2.setOnClickListener {

            val test = cardiogaphCommunication.FlashRead()
            Log.d("TAG", test.toString())
        }

          /*   // тестовое отображение графика ЭКГ на фиктивных данных
        binding.onClickStartSession.setOnClickListener {
            binding.onClickStartSession.text = "Stop"
            //val ecgShowView = EcgShowView(this, null)
            val ecgShowView = binding.ecgShowView
            //заменить на реальные данные, полученные с помощью MonitoringMode
            var health_data = "0.101253886148333549,0.0" +
                    "01253886148333549,0.003036087844520807,0.002440808573737741,-0.01077798567712307,-0.02941250056028366,-0.02127" +
                    "940207719803,0.03379339724779129,0.09094017744064331,0.05208359658718109,-0.1133501008152962,-0.2622079849243" +
                    "164,-0.1582041531801224,0.2832704782485962,0.8482071161270142,1.202911853790283,1.217478036880493,1.0064306259" +
                    "15527,0.5614029765129089,-0.5808430314064026,-3.093567848205566,-7.158360481262207,-12.06765747070312,-16.67280" +
                    "197143555,-20.29765510559082,-23.17350387573242,-25.97895050048828,-29.04569435119629,-32.08361053466797,-34.611" +
                    "01150512695,-36.49107360839844,-37.83483505249023,-39.2723503112793,-40.31753540039062,-41.73135375976562,-44.40" +
                    "776443481445,-48.18602752685547,-50.971923828125,-49.85507965087891,-44.25748062133789,-38.60691070556641,-40.88" +
                    "425827026367,-55.82911682128906,-75.75099182128906,-75.57111358642578,-20.18935394287109,112.4039306640625,302.4" +
                    "443969726562,478.810302734375,549.542724609375,462.8594360351562,253.620361328125,29.86661911010742,-99.094490" +
                    "05126953,-97.12584686279297,-16.40250968933105,55.83222579956055,67.73938751220703,30.61519622802734,-9.7101583" +
                    "48083496,-20.65663146972656,-5.159292697906494,12.56078052520752,15.47367382049561,5.182388305664062,-4.5807843" +
                    "20831299,-4.939818382263184,1.16289758682251,5.320072650909424,3.525331497192383,-0.9494332075119019,-2.7819726" +
                    "46713257,-0.7593205571174622,1.852165102958679,1.934446334838867,-0.02557146549224854,-1.326619863510132,-0.662" +
                    "1173620223999,0.7012819647789001,1.002269268035889,0.1525551378726959,-0.5706530809402466,-0.3565154075622559," +
                    "0.3104846179485321,0.5506284832954407,0.2197907865047455,-0.1195622012019157,-0.04902923107147217,0.2566174864" +
                    "768982,0.403188943862915,0.3047243356704712,0.1968544721603394,0.2810082137584686,0.5909113883972168,0.5504930" +
                    "019378662,0.9481502175331116,0.3396361768245697,-0.9867785573005676,-1.6379554271698,0.3059036731719971,4.8029" +
                    "6802520752,7.988834857940674,4.358085632324219,-7.128910541534424,-18.30419731140137,-13.80635738372803,19.7751" +
                    "1596679688,82.21820831298828,152.1483306884766,190.4971923828125,159.1590728759766,49.01980972290039,-102.2764" +
                    "30415726,0.07632210850715637,0.04921660572290421,0.02271043695509434,"

            ecgShowView.setData(health_data)
        }*/

    }





    private fun BluetoothClassicConnect(): BluetoothSocket? {

        // Checking Bluetooth availability
        //val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            // The device does not support Bluetooth
            Log.d("BluetoothClassic", "Device does not support Bluetooth")
        }

        // Enabling Bluetooth
        if (!bluetoothAdapter?.isEnabled!!) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // Bluetooth enabled
                    Log.d("BluetoothClassic", "Bluetooth enabled")
                } else {
                    // The user declined the request to turn on Bluetooth
                    Log.d("BluetoothClassic", "User declined to enable Bluetooth")
                }
            }.launch(enableBtIntent)
        }

        // Device search
        if (ActivityCompat.checkSelfPermission( // вручную настроить разрешения
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
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
            cardiogaphCommunication.setStreams(socket?.inputStream, socket?.outputStream)
            Log.d("BluetoothClassic", "Connected to device")
        }.start()

        return socket
    }

    private fun BluetoothClassicDisconnect(socket: BluetoothSocket) {
        // Закрыть Bluetooth-сокет и освободить ресурсы
        inputStream?.close()
        outputStream?.close()
        socket?.close()
        Log.d("BluetoothClassic", "Отключение от устройства")
    }

    private fun getBondedDevices() {
        try {
            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach { device ->
                // Используем имя и адрес устройства
                val deviceName = device.name
                val deviceHardwareAddress = device.address
                Log.d("Bluetooth", "Устройство: $deviceName, MAC: $deviceHardwareAddress")
            }
        } catch (e: SecurityException) {
            // Обрабатываем исключение безопасности, если разрешение не предоставлено
            Log.e("Bluetooth", "Ошибка доступа к Bluetooth-устройствам: ${e.message}")
        }
    }
}


