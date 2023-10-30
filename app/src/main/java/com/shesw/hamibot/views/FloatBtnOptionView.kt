package com.shesw.hamibot.views

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hamibot.hamibot.R
import com.hamibot.hamibot.external.ScriptIntents
import com.hamibot.hamibot.ui.floating.FloatyWindowManger
import com.shesw.hamibot.CslConst
import java.util.ArrayList

class FloatBtnOptionView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val accounts = arrayListOf(
        "qatestls2@163.com", // 普通付费礼物
        "pantest001@163.com",   // 有高粉解锁
        "qatest7@163.com"
    )

    private val passwords = arrayListOf(
        "qa1234",
        "JNpj123456",
        "qa1234",
    )

    private fun script(model: AccountModel): String =
        "className('android.widget.EditText').findOnce(0).setText(\'${model.account}\')\n" +
            "className('android.widget.EditText').findOnce(1).setText(\'${model.password}\')\n" +
            "click('下一步')"

    private val runScript: (model: AccountModel) -> Unit = {
        val intent = Intent()
        intent.putExtra(ScriptIntents.EXTRA_KEY_PRE_EXECUTE_SCRIPT, script(it))
        intent.putExtra(ScriptIntents.EXTRA_KEY_LOOP_TIMES, 1)
        intent.putExtra(ScriptIntents.EXTRA_KEY_DELAY, 0)
        intent.putExtra(ScriptIntents.EXTRA_KEY_LOOP_INTERVAL, 0)
        ScriptIntents.handleIntent(context, intent)
    }

    companion object {
        fun createWindowLayoutParams(): WindowManager.LayoutParams? {
            val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                FloatyWindowManger.getWindowType(), 520, -3)
            layoutParams.gravity = Gravity.LEFT or Gravity.TOP
            return layoutParams
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.float_btn_optn_view, this, true)?.run {
            findViewById<RecyclerView>(R.id.account_rv).let {
                it.layoutManager = GridLayoutManager(context, 3)
                it.adapter = AccountAdapter(ArrayList<AccountModel>().also { list ->
                    for (i in 0 until accounts.size) {
                        list.add(AccountModel(accounts[i], passwords[i]))
                    }
                }, runScript)
            }
        }
    }

}