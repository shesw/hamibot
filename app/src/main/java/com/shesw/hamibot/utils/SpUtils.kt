package com.shesw.hamibot.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SpUtils {

    private const val NAME = "csl_sp_utils"

    private const val lofter_account_sp_key = "lofter_account_sp_key"

    suspend fun getLofterAccount(context: Context): String? {
        return withContext(Dispatchers.IO) {
            val sharedPreferences = context.getSharedPreferences(NAME, MODE_PRIVATE)
            sharedPreferences.getString(lofter_account_sp_key, null)
        }
    }

    suspend fun putLofterAccount(context: Context, v: String) {
        return withContext(Dispatchers.IO) {
            val sharedPreferences = context.getSharedPreferences(NAME, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(lofter_account_sp_key, v)
            editor.apply()
        }
    }

}