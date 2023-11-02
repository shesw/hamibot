package com.shesw.hamibot.sg_ocr

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class CaptureAndOcrManagerService : Service() {

    companion object {
        private const val TAG = "CAOManagerTAG"
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val data = intent?.getStringExtra("data")
        Log.d(TAG, "onStartCommand, data:$data")

        return super.onStartCommand(intent, flags, startId)
    }
}