package com.iomt.android.compose.view.main

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iomt.android.bluetooth.BluetoothDeviceWithConfig
import com.iomt.android.compose.components.DeviceList
import com.iomt.android.compose.navigation.NavRouter
import com.iomt.android.compose.theme.colorScheme
import com.iomt.android.utils.*

@RequiresApi(Build.VERSION_CODES.S)
@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
@Composable
fun HomeView(mutableFloatingButtonBuilder: MutableFloatingButtonBuilder, onKnownDeviceClick: (BluetoothDeviceWithConfig) -> Unit) {
    var currentScreen by remember { mutableStateOf("Home") }

    mutableFloatingButtonBuilder.value = { navController ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            if (currentScreen == "Home") {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Button(
                        onClick = {
                            currentScreen = "Cardio"
                            navController.navigate(NavRouter.Main.Cardio.path)
                        },
                        shape = ShapeDefaults.Medium,
                        modifier = Modifier
                            .height(57.dp)
                            .width(97.dp)
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFC2E8FF),
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Cardio")
                    }



                    ExtendedFloatingActionButton(
                        onClick = { navController.navigate(NavRouter.Main.BleScanner) },
                        shape = ShapeDefaults.Medium,
                    ) {
                        Icon(Icons.Default.Add, "Add")
                        Text("Scan")
                    }
                }
            }
        }
    }

    val bluetoothLeForegroundService by rememberBoundService().collectAsState()
    withLoading(bluetoothLeForegroundService) { foregroundService ->
        val knownDevicesWithConfigs = remember { foregroundService.getConnectedDevicesWithConfigs().toMutableStateList() }
        Column(Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            DeviceList("Known devices", knownDevicesWithConfigs) { onKnownDeviceClick(it) }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
@Preview
@Composable
private fun HomeViewPreview() {
    val mutableFloatingButtonBuilder = remember { mutableStateOf<FloatingButtonBuilder>({}) }
    val connectedDevices = remember { mutableStateListOf<BluetoothDeviceWithConfig>() }
    MaterialTheme(colorScheme) { HomeView(mutableFloatingButtonBuilder) { connectedDevices.add(it) } }
}
