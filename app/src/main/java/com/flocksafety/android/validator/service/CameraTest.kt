package com.flocksafety.android.validator.service

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * Service for managing camera commands.
 */
class CameraTest : Test(
    name = "Camera Tester",
    category = "flock.intent.category.CAMERA_TEST",
    actions = listOf(
        SHOW_CAMERA_ACTIVITY
    )
) {
    companion object {
        // Command to show the camera activity to the user.
        const val SHOW_CAMERA_ACTIVITY = "flock.intent.action.SHOW_CAMERA_ACTIVITY"

        // Command action sent when the service ends.
        const val SHUT_DOWN_ACTION = "com.flocksafety.android.validator.STOP_CAMERA_TESTS"
    }

    private val binder = TestBinder()

    override fun stop() {
        sendBroadcast(Intent(SHUT_DOWN_ACTION), "com.flocksafety.android.validator")

        // do this at the end because we're setting an output field here sometimes
        super.stop()
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun handleAction(context: Context, intent: Intent) {
        when (intent.action) {
            SHOW_CAMERA_ACTIVITY -> {
                val cameraActivityIntent = Intent(context, CameraActivity::class.java)
                cameraActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(cameraActivityIntent)
            }
        }
    }

    inner class TestBinder : Binder()
}
