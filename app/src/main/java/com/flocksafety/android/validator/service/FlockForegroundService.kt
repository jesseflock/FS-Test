package com.flocksafety.android.validator.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Handler
import android.os.IBinder
import java.util.*

abstract class FlockForegroundService(
    /** pretty service name string (e.g. "Sensor Service") */
    protected val serviceName: String
) : Service() {

    abstract fun create()

    abstract fun start()

    abstract fun stop()

    companion object {
        private const val foregroundId: Int = 1337
    }
    private var hasStarted = false
    private lateinit var supervisorHandler: Handler

    final override fun onCreate() {
        super.onCreate()

        // make ourselves a foreground service (needs notification)
        val serviceChannelName = serviceName.toLowerCase(Locale.getDefault()).replace(" ", "_")
        val channelId = createNotificationChannel(serviceChannelName, serviceName)
        val notification: Notification = Notification.Builder(this, channelId)
            .setContentTitle(serviceName)
            .build()

        create()
    }

    final override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        start()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    final override fun onDestroy() {
        stop()
        super.onDestroy()
    }

    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_LOW)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private val pmFlags = PackageManager.GET_ACTIVITIES or
            PackageManager.GET_INTENT_FILTERS or
            PackageManager.GET_PERMISSIONS or
            PackageManager.GET_URI_PERMISSION_PATTERNS or
            PackageManager.GET_SERVICES or
            PackageManager.GET_RECEIVERS or
            PackageManager.GET_META_DATA

}