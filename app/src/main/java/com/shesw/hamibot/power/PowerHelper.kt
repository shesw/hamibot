package com.shesw.hamibot.power

import android.annotation.SuppressLint
import android.content.Context
import android.os.PowerManager
import android.util.Log
import java.lang.Exception

object PowerHelper {

    const val tag = "csl:power_helper_tag"

    private var wakeLock: PowerManager.WakeLock? = null

    @SuppressLint("WakelockTimeout")
    @JvmStatic
    fun lightOnScreen(context: Context) {
        try {
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, tag)
            wakeLock?.acquire()
            Log.d(tag, "lightOnScreen")
        } catch (e: Exception) {
            Log.e(tag, "lightOnScreen, error=$e")
        }
    }

    @JvmStatic
    fun lightOffScreen() {
        try {
            wakeLock?.release()
            Log.d(tag, "lightOffScreen")
        } catch (e: Exception) {
            Log.e(tag, "lightOffScreen, error=$e")
        }
    }

}