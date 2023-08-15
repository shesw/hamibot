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
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hamibot.hamibot.Pref
import com.hamibot.hamibot.R
import com.hamibot.hamibot.external.ScriptIntents
import com.shesw.hamibot.CslConst
import com.stardust.util.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.FileOutputStream

class CSLMainActivity : Activity() {

    companion object {
        private const val TAG = "CSLMainActivityTAG"
    }

    private var etUrl: EditText? = null
    private var etName: EditText? = null
    private var needRequest: View? = null

    private val okhttpClient = OkHttpClient()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            run(etName?.text?.toString())
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
        needRequest = findViewById<View>(R.id.needRequest)?.apply {
            setOnClickListener {
                isSelected = !isSelected
            }
        }
        needRequest?.isSelected = true

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

        findViewById<View>(R.id.run)?.setOnClickListener { run(etName?.text?.toString()) }
        findViewById<View>(R.id.cancel_proxy)?.setOnClickListener { run("cancel_proxy") }
        findViewById<View>(R.id.set_proxy)?.setOnClickListener { run("set_proxy") }

    }

    private fun run(fileNameStr: String?) {
        GlobalScope.launch {
            try {
                var fileName = fileNameStr ?: return@launch
                if (needRequest?.isSelected == true && !NetworkUtils.isNetWorkAvailable(this@CSLMainActivity)) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@CSLMainActivity,
                            "network not available",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@launch
                }
                if (fileName.endsWith(".js")) {
                    fileName = fileName.substring(0, fileName.length - 3)
                }
                withContext(Dispatchers.IO) {
                    Pref.def().edit().putString("${TAG}_url", etUrl?.text?.toString() ?: "")
                        .apply()
                    Pref.def().edit().putString("${TAG}_name", fileName).apply()
                }
                if (needRequest?.isSelected == true) {
                    downloadJS(fileName)
                }

                val intent = Intent()
                intent.putExtra(ScriptIntents.EXTRA_KEY_PATH, "${CslConst.dirPath}$fileName.js")
                intent.putExtra(ScriptIntents.EXTRA_KEY_LOOP_TIMES, 1)
                intent.putExtra(ScriptIntents.EXTRA_KEY_DELAY, 0)
                intent.putExtra(ScriptIntents.EXTRA_KEY_LOOP_INTERVAL, 0)
                withContext(Dispatchers.Main) {
                    ScriptIntents.handleIntent(this@CSLMainActivity, intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CSLMainActivity,
                        e.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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

                val fos = FileOutputStream("${CslConst.dirPath}$fileName.js")
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