package com.flocksafety.android.validator

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.core.app.ApplicationProvider

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private val appContext: Context = ApplicationProvider.getApplicationContext()
    @Test
    fun useAppContext() {
        // Context of the app under test.
        assertEquals("com.flocksafety.android.validator", appContext.packageName)
    }
}
