package com.shesw.hamibot.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

object RequestUtils {

    private const val TAG = "RequestUtilsTAG"

    private val okhttpClient = OkHttpClient()

    private var ap: String? = null

    suspend fun getLofterAccounts(context: Context): String {
        if (!TextUtils.isEmpty(ap)) {
            return ap!!
        }
        val spValue = SpUtils.getLofterAccount(context)
        if (!TextUtils.isEmpty(spValue)) {
            ap = spValue
            return ap!!
        }
        val requestRes = getLofterAccountsByRequest()
        SpUtils.putLofterAccount(context, requestRes)
        ap = requestRes
        return ap ?: ""
    }

    suspend fun getLofterAccountsByRequest(): String {
        return withContext(Dispatchers.IO) {
            val url = "http://10.242.132.241:7778/lofter_accounts"
            val request = Request.Builder().url(url).build()
            val call = okhttpClient.newCall(request)
            val response = call.execute()

            if (response.isSuccessful) {
                response.body()?.string() ?: ""
            } else {
                Log.d(TAG, "fail")
                ""
            }
        }
    }
}