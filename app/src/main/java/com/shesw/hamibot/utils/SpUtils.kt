package com.shesw.hamibot.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.stardust.app.GlobalAppContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SpUtils {

    private const val NAME = "csl_sp_utils"

    private const val lofter_account_sp_key = "lofter_account_sp_key"

    suspend fun putValue(key: String, value: String) {
        return withContext(Dispatchers.IO) {
            val sharedPreferences = GlobalAppContext.get().getSharedPreferences(NAME, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }

    suspend fun getValue(key: String): String? {
        return withContext(Dispatchers.IO) {
            val sharedPreferences = GlobalAppContext.get().getSharedPreferences(NAME, MODE_PRIVATE)
            sharedPreferences.getString(key, null)
        }
    }

    suspend fun getLofterAccount(): String? {
        return getValue(lofter_account_sp_key)
    }

    suspend fun putLofterAccount(v: String) {
        return putValue(lofter_account_sp_key, v)
    }

}