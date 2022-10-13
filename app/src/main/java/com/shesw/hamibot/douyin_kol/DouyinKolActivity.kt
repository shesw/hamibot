package com.shesw.hamibot.douyin_kol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hamibot.hamibot.R
import com.hamibot.hamibot.external.ScriptIntents

class DouyinKolActivity : AppCompatActivity(), View.OnClickListener {

    private var btnRun: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_douyin_kol)
        findView()
        initView()
        init()
    }

    private fun findView() {
        btnRun = findViewById(R.id.run)
    }

    private fun initView() {
        btnRun?.setOnClickListener(this)
    }

    private fun init() {

    }

    private fun run() {
        val intent = Intent()
//        intent.putExtra(ScriptIntents.EXTRA_KEY_PRE_EXECUTE_SCRIPT, scrips);
        //        intent.putExtra(ScriptIntents.EXTRA_KEY_PRE_EXECUTE_SCRIPT, scrips);
        intent.putExtra(ScriptIntents.EXTRA_KEY_PATH, "/storage/emulated/0/myhamibot.js")
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