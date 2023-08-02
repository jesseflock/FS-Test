package com.flocksafety.android.validator.service

import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.os.*
import com.flocksafety.android.validator.model.MidAssemblyTestResponse

class MidAssemblyTest : Test(
    name = "Mid Assembly Tester",
    category = "flock.intent.category.MID_ASSEMBLY_TEST",
    actions = listOf(
        WIFI_AP_SCAN,
        BLE_SCAN
    )
) {
    companion object {
        const val WIFI_AP_SCAN = "flock.intent.action.WIFI_AP_SCAN"
        const val BLE_SCAN = "flock.intent.action.BLE_SCAN"
        const val SHUT_DOWN_ACTION = "com.flocksafety.android.validator.STOP_CAMERA_TESTS"
    }

    private val model = MidAssemblyTestResponse()
    private val binder = TestBinder()
    private lateinit var wifiManager: WifiManager
    private lateinit var bluetoothManager: BluetoothManager

    override fun create() {
        super.create()

        wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }

    override fun stop() {
        sendBroadcast(Intent(SHUT_DOWN_ACTION), "com.flocksafety.android.validator")

        // do this at the end because we're setting an output field here sometimes
        super.stop()
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun handleAction(context: Context, intent: Intent) {
        when (intent.action) {
            WIFI_AP_SCAN -> {
                val ssid = intent.getStringExtra("SSID")

                val intentFilter = IntentFilter()
                intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)

                registerReceiver(object : BroadcastReceiver() {
                    override fun onReceive(context: Context, intent: Intent) {
                        unregisterReceiver(this)

                        wifiManager.scanResults.forEach {
                            if (it.SSID == ssid) {
                                model.wifiFound = true
                                model.wifiRssi = it.level
                            }
                        }
                    }
                }, intentFilter)

                model.wifiFound = false
                wifiManager.startScan()
            }
            BLE_SCAN -> {
                val bleAdd = intent.getStringExtra("BLEADD")

                val bluetoothLeScanner = bluetoothManager.adapter.bluetoothLeScanner
                val handler = Handler()

                val leScanCallback: ScanCallback = object : ScanCallback() {
                    override fun onScanResult(callbackType: Int, result: ScanResult) {
                        super.onScanResult(callbackType, result)

                        if (result.device.address == bleAdd) {
                            model.bleFound = true
                            model.bleRssi = result.rssi
                        }
                    }
                }

                val scanPeriod: Long = 10000
                handler.postDelayed({
                    bluetoothLeScanner.stopScan(leScanCallback)
                }, scanPeriod)
                bluetoothLeScanner.startScan(leScanCallback)
                model.bleFound = false
            }
        }
    }

    inner class TestBinder : Binder() { }
}
