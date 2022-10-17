package com.shesw.hamibot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hamibot.hamibot.R
import com.hamibot.hamibot.external.ScriptIntents
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder

abstract class CslBaseActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "CslBaseActivityTAG"

    private var tvTitle: TextView? = null
    private var btnRun: View? = null
    private var btnConfig: View? = null
    private var btnConfigClose: View? = null
    private var configLayout: ConstraintLayout? = null
    private var configItems: LinearLayout? = null
    private var newConfigLine: View? = null
    private var etAddConfigKey: EditText? = null
    private var etAddConfigValue: EditText? = null
    private var btnAddConfig: View? = null

    private var configMap = HashMap<String, String>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            run()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.csl_base_activity_layout)

        val file = File(CslConst.dirPath)
        if (!file.exists()) {
            file.mkdir()
        }

        findView()
        initView()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("start_run_scripts"))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    abstract fun pageTitle(): String

    abstract fun scriptsName(): String

    open fun findView() {
        btnRun = findViewById(R.id.run)
        btnConfig = findViewById(R.id.config)
        tvTitle = findViewById(R.id.title)
        configLayout = findViewById(R.id.config_layout)
        btnConfigClose = findViewById(R.id.config_close)
        configItems = findViewById(R.id.config_items)

        newConfigLine = findViewById(R.id.new_config_line)
        etAddConfigKey = newConfigLine?.findViewById(R.id.config_name_value)
        etAddConfigValue = newConfigLine?.findViewById(R.id.config_value_value)
        btnAddConfig = newConfigLine?.findViewById(R.id.btn_config_confirm)
    }

    open fun initView() {
        tvTitle?.text = pageTitle()
        btnRun?.setOnClickListener(this)
        btnConfig?.setOnClickListener(this)
        btnConfigClose?.setOnClickListener(this)
        btnConfig?.setOnClickListener(this)
        btnAddConfig?.setOnClickListener(this)

        // 初始化配置
        initConfigs()
    }

    private fun run() {
        val intent = Intent()
        intent.putExtra(ScriptIntents.EXTRA_KEY_PATH, CslConst.dirPath + scriptsName())
        intent.putExtra(ScriptIntents.EXTRA_KEY_LOOP_TIMES, 1)
        intent.putExtra(ScriptIntents.EXTRA_KEY_DELAY, 0)
        intent.putExtra(ScriptIntents.EXTRA_KEY_LOOP_INTERVAL, 0)
        ScriptIntents.handleIntent(this, intent)
    }

    private fun initConfigs() {
        var br: BufferedReader? = null
        try {
            val file = File("${CslConst.dirPath}${scriptsName()}_config.txt")
            if (!file.exists()) {
                return
            }
            br = BufferedReader(FileReader(file))
            val configLine = br.readLine()
            if (!TextUtils.isEmpty(configLine)) {
                configLine.split("&").forEach {
                    val pair = it.split(":")
                    val key = pair[0]
                    val value = pair[1]
                    configMap[key] = value
                    addConfigLayout(key, value)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        } finally {
            br?.close()
        }
    }

    private fun addConfigConfirm() {
        val key = etAddConfigKey?.text?.toString()
        val value = etAddConfigValue?.text?.toString()
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            Toast.makeText(this, "请输入参数", Toast.LENGTH_LONG).show()
            return
        }
        if (configMap.containsKey(key)) {
            Toast.makeText(this, "配置已存在", Toast.LENGTH_LONG).show()
            return
        }
        configMap[key!!] = value!!
        writeConfigMapToLocal()
        addConfigLayout(key, value)
    }

    private fun addConfigLayout(key: String, value: String) {
        val itemLayout = LayoutInflater.from(this).inflate(R.layout.csl_base_config_item_layout, null, false)
        itemLayout?.findViewById<EditText>(R.id.config_name_value)?.run {
            setText(key)
            isEnabled = false
        }
        itemLayout?.findViewById<EditText>(R.id.config_value_value)?.setText(value)
        itemLayout?.findViewById<View>(R.id.btn_config_confirm)?.setOnClickListener { configUpdateByUI(it) }
        configItems?.addView(itemLayout)
    }

    private fun configUpdateByUI(view: View) {
        val parent = view.parent as ConstraintLayout
        val key = parent.findViewById<EditText>(R.id.config_name_value)?.text?.toString() ?: return
        val value = parent.findViewById<EditText>(R.id.config_value_value)?.text?.toString()
        if (TextUtils.isEmpty(value)) {
            configMap.remove(key)
            configItems?.removeView(parent)
        } else {
            configMap[key] = value!!
        }
        writeConfigMapToLocal()
    }

    private fun writeConfigMapToLocal() {
        var bw: BufferedWriter? = null
        try {
            val file = File("${CslConst.dirPath}${scriptsName()}_config.txt")
            bw = BufferedWriter(FileWriter(file))
            val sb = StringBuilder()
            for (key in configMap.keys) {
                sb.append(key).append(":").append(configMap[key]).append("&")
            }
            bw.write(sb.toString())
            bw.flush()
            Toast.makeText(this, "更新成功", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            Toast.makeText(this, "更新失败", Toast.LENGTH_LONG).show()
        } finally {
            bw?.close()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.run -> run()
            R.id.config -> configLayout?.visibility = View.VISIBLE
            R.id.config_close -> configLayout?.visibility = View.GONE
            R.id.btn_config_confirm -> addConfigConfirm()
        }
    }
}
