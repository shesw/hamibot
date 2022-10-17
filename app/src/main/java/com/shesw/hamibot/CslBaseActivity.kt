package com.shesw.hamibot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.hamibot.hamibot.R
import com.hamibot.hamibot.external.ScriptIntents

abstract class CslBaseActivity : AppCompatActivity(), View.OnClickListener {

    private var btnRun: View? = null

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            run()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        findView()
        initView()
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("start_run_scripts"))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    abstract fun getLayoutId(): Int

    abstract fun scriptsName(): String

    open fun findView() {
        btnRun = findViewById(R.id.run)
    }

    open fun initView() {
        btnRun?.setOnClickListener(this)
    }

    private fun run() {
        val intent = Intent()
        intent.putExtra(ScriptIntents.EXTRA_KEY_PATH, CslConst.dirPath + scriptsName())
        intent.putExtra(ScriptIntents.EXTRA_KEY_LOOP_TIMES, 1)
        intent.putExtra(ScriptIntents.EXTRA_KEY_DELAY, 0)
        intent.putExtra(ScriptIntents.EXTRA_KEY_LOOP_INTERVAL, 0)
        ScriptIntents.handleIntent(this, intent)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.run -> run()
        }
    }
}
