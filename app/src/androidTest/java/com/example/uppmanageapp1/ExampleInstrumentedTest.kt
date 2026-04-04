package com.example.uppmanageapp1

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

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

    @Test
    fun appContextPackageNameIsCorrect() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // Allow for productFlavor applicationId suffixes (e.g. .developmentextra)
        assertTrue(appContext.packageName.startsWith("com.example.uppmanageapp1"))
    }

}