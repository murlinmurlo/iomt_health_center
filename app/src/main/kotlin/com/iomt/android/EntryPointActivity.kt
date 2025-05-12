package com.iomt.android

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.iomt.android.compose.EntryPoint

import com.iomt.android.compose.view.bt.BtScannerView

/**
 * [AppCompatActivity] that is used for [EntryPoint] - compose integration into android
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class EntryPointActivity : AppCompatActivity() {
    private val REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        if (!isNotificationPermissionAllowed(this)) {
            showToast(getString(R.string.notifications_disabled))
            requestNotificationPermission()
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FCM_DEBUG", "Токен: ${task.result}")
            } else {
                Log.e("FCM_DEBUG", "Ошибка: ${task.exception}")
            }
        }



        //
        var bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
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

        val bluetoothManager: BluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            val REQUEST_ENABLE_BT = 1
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    //
        if (!checkPermission()) {
            requestPermissions(permissions.toTypedArray(), MASTER_PERMISSION_REQUEST_CODE)
        }

        Log.d(loggerTag, "EntryPointActivity has successfully started")
        setContent {
            EntryPoint()
            //BtScannerView()
        }
    }

    private fun isNotificationPermissionAllowed(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    private fun requestNotificationPermission() {
        val intent = Intent().apply {
            action = "android.settings.APP_NOTIFICATION_SETTINGS"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra("android.provider.extra.APP_PACKAGE", packageName)
            } else {
                putExtra("app_package", packageName)
                putExtra("app_uid", applicationInfo.uid)
            }
        }
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (isNotificationPermissionAllowed(this)) {
                showToast(getString(R.string.notifications_enabled))
            } else {
                showToast(getString(R.string.notifications_disabled))
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkPermission() = permissions.all { permission ->
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val permissions = buildList {
            add(Manifest.permission.BLUETOOTH)
            add(Manifest.permission.BLUETOOTH_SCAN)
            add(Manifest.permission.BLUETOOTH_CONNECT)
            add(Manifest.permission.BLUETOOTH_PRIVILEGED)
            add(Manifest.permission.FOREGROUND_SERVICE)
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                add(Manifest.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE)
            }
        }

        private const val MASTER_PERMISSION_REQUEST_CODE = 150_601

        @Suppress("EMPTY_BLOCK_STRUCTURE_ERROR")
        private val loggerTag = object { }.javaClass.enclosingClass.simpleName
    }
}
