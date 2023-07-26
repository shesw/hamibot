package com.shesw.hamibot.csl_main


import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.hamibot.hamibot.Pref
import com.hamibot.hamibot.R
import com.hamibot.hamibot.external.ScriptIntents
import com.shesw.hamibot.CslConst
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.FileOutputStream
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class CSLMainActivity : Activity() {

    companion object {
        private const val TAG = "CSLMainActivityTAG"
    }

    private var etUrl: EditText? = null
    private var etName: EditText? = null

    private val okhttpClient = OkHttpClient()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            run()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_csl_main_layout)

        etUrl = findViewById(R.id.etUrl)
        etName = findViewById(R.id.etName)

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(receiver, IntentFilter("start_run_scripts"))

        GlobalScope.launch {
            val url = withContext(Dispatchers.IO) {
                Pref.def().getString("${TAG}_url", "")
            }
            val name = withContext(Dispatchers.IO) {
                Pref.def().getString("${TAG}_name", "")
            }
            etUrl?.setText(url)
            etName?.setText(name)
        }

        findViewById<View>(R.id.run)?.setOnClickListener {
            run()
        }
    }

    private fun run() {
        GlobalScope.launch {
            var fileName = etName?.text?.toString() ?: return@launch
            if (fileName.endsWith(".js")) {
                fileName = fileName.substring(0, fileName.length - 3)
            }

            withContext(Dispatchers.IO) {
                Pref.def().edit().putString("${TAG}_url", etUrl?.text?.toString() ?: "").apply()
                Pref.def().edit().putString("${TAG}_name", fileName).apply()
            }

            downloadJS(fileName)
            val intent = Intent()
            intent.putExtra(ScriptIntents.EXTRA_KEY_PATH, "${CslConst.dirPath}/$fileName.js")
            intent.putExtra(ScriptIntents.EXTRA_KEY_LOOP_TIMES, 1)
            intent.putExtra(ScriptIntents.EXTRA_KEY_DELAY, 0)
            intent.putExtra(ScriptIntents.EXTRA_KEY_LOOP_INTERVAL, 0)
            ScriptIntents.handleIntent(this@CSLMainActivity, intent)
        }
    }

    private suspend fun downloadJS(fileName: String) {
        withContext(Dispatchers.IO) {
            val url =
                "http://${etUrl?.text?.toString() ?: ""}/download?n=$fileName.js"
            Log.i(TAG, "url: $url")
            val request = Request.Builder().url(url).build()
            val call = okhttpClient.newCall(request)
            val response = call.execute()

            if (response.isSuccessful) {
                val inputStream = response.body()?.byteStream() ?: return@withContext

                val fos = FileOutputStream("${CslConst.dirPath}/$fileName.js")
                val buffer = ByteArray(1024)
                var len: Int
                while (inputStream.read(buffer).also { len = it } != -1) {
                    fos.write(buffer, 0, len)
                }
                fos.flush()
                fos.close()
                inputStream.close()
            }
        }
    }
}