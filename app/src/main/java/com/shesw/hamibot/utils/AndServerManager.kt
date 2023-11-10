package com.shesw.hamibot.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.util.Log
import com.stardust.app.GlobalAppContext
import com.yanzhenjie.andserver.AndServer
import java.util.concurrent.TimeUnit

object AndServerManager {

    private var server = AndServer.webServer(GlobalAppContext.get())
        .port(1111)
        .timeout(10, TimeUnit.MILLISECONDS)
        .build()

    @JvmStatic
    fun start() {
        Log.d("csldebug", getLocalIpAddress())
        if (server.isRunning) {
            return
        }
        server.startup()
    }

    @JvmStatic
    fun shutdown() {
        if (server.isRunning) {
            server.shutdown()
        }
    }


    /**
     * 检查网络是否可用
     *
     * @param paramContext
     * @return
     */
    fun checkEnableWifi(): Boolean {
        @SuppressLint("WrongConstant") val localNetworkInfo = (GlobalAppContext.get().getSystemService("connectivity") as? ConnectivityManager)?.activeNetworkInfo
        return (localNetworkInfo != null) && (localNetworkInfo.isAvailable)
    }

    /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    fun getLocalIpAddress(): String {
        try {
            val wifiManager = GlobalAppContext.get().applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            val wifiInfo = wifiManager?.connectionInfo
            val i = wifiInfo?.ipAddress ?: return "0"
            return int2ip(i)
        } catch (e: Exception) {
            return "0"
        }
    }

    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return
     */
    private fun int2ip(ipInt: Int): String {
        val sb = StringBuilder()
        sb.append(ipInt and 0xFF).append(".")
        sb.append((ipInt shr 8) and 0xFF).append(".")
        sb.append((ipInt shr 16) and 0xFF).append(".")
        sb.append((ipInt shr 24) and 0xFF)
        return sb.toString()
    }

}