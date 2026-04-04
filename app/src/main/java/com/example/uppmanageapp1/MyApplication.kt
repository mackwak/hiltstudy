package com.example.uppmanageapp1

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (isMainProcess()) {
            FirebaseApp.initializeApp(this)
        }
    }



    private fun isMainProcess(): Boolean {
        val pid = android.os.Process.myPid()
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processes = am.runningAppProcesses ?: return true
        for (proc in processes) {
            if (proc.pid == pid) {
                return proc.processName == packageName
            }
        }
        return true
    }
}

