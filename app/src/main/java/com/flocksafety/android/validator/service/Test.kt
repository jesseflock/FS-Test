package com.flocksafety.android.validator.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
import java.lang.Exception

abstract class Test(
    private val name: String,
    protected val category: String,
    protected val actions: List<String>) : FlockForegroundService(
    serviceName = name
) {
    companion object {
        const val STOP_ACTION = "flock.intent.action.STOP_TEST"

        const val SERVICE_NAME = "Test"
    }

    private val controller: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) = try {when (intent.action) {
                STOP_ACTION -> stopSelf()
                else -> handleAction(context, intent)
            }
        } catch (e: Exception) {

        }
    }

    /**
     * Make sure to call super, this sets up the broadcast receiver for communication
     */
    override fun create() {
        Log.i(SERVICE_NAME,"Creating $name")

        val filter = IntentFilter()
        actions.plus(STOP_ACTION).forEach { filter.addAction(it) }
        filter.addCategory(category)

        applicationContext.registerReceiver(controller, filter)
    }

    override fun start() { }

    /**
     * Make sure to call super, this avoids memory leaks
     */
    override fun stop() {
        Log.i(SERVICE_NAME, "Stopping $name")

        applicationContext.unregisterReceiver(controller)
    }

    /**
     * Handler function for intents received from the broadcast receiver
     */
    abstract fun handleAction(context: Context, intent: Intent)
}
