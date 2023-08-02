package com.flocksafety.android.validator.service

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import com.flocksafety.android.validator.R

/**
 * Camera activity that displays camera images to the user.
 */
class CameraActivity : Activity() {
    private lateinit var textureView: TextureView
    private lateinit var sampleBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_layout)

        sampleBitmap = BitmapFactory.decodeResource(resources, R.drawable.robot_waving)

        textureView = findViewById(R.id.cameraPreview)
        textureView.surfaceTextureListener = object : SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                surfaceTexture: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                configureSurfaceTexture()
            }

            override fun onSurfaceTextureSizeChanged(
                surfaceTexture: SurfaceTexture,
                width: Int,
                height: Int
            ) {
                configureSurfaceTexture()
            }

            override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
                // Nothing required.
                return false
            }

            override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {
                // Nothing required.
            }
        }
    }

    private fun configureSurfaceTexture() {
        loadScaledSampleBitmap()

        val surface = Surface(textureView.surfaceTexture)
        try {
            textureView.surfaceTexture.setDefaultBufferSize(textureView.width, textureView.height)

            val canvas = surface.lockCanvas(null)
            canvas.drawBitmap(sampleBitmap, 0f, 0f, null)
            surface.unlockCanvasAndPost(canvas)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Illegal argument passed when drawing image.", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Texture view not yet ready for drawing.", e)
        } finally {
            surface.release()
        }
    }

    private fun loadScaledSampleBitmap() {
        sampleBitmap = BitmapFactory.decodeResource(resources, R.drawable.robot_waving)

        val scaleFactor = minOf(
            textureView.width.toFloat() / sampleBitmap.width.toFloat(),
            textureView.height.toFloat() / sampleBitmap.height.toFloat()
        )

        val targetWidth = (sampleBitmap.width * scaleFactor).toInt()
        val targetHeight = (sampleBitmap.height * scaleFactor).toInt()

        sampleBitmap = Bitmap.createScaledBitmap(sampleBitmap, targetWidth, targetHeight, true)
    }

    companion object {
        private const val TAG = "CameraActivity"
    }
}
