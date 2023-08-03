package com.flocksafety.android.validator.service

import android.app.Activity
import android.os.Bundle
import com.flocksafety.android.validator.R

class CameraActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_layout)
    }
}
