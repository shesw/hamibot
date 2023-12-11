package com.shesw.hamibot.views

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hamibot.hamibot.R
import com.hamibot.hamibot.external.ScriptIntents
import com.hamibot.hamibot.ui.floating.FloatyWindowManger
import com.shesw.hamibot.utils.AndServerManager
import com.shesw.hamibot.utils.JSEngineHelper
import com.shesw.hamibot.utils.RequestUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class FloatBtnOptionView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private fun laScript(model: AccountModel): String = "className('android.widget.EditText').findOnce(0).setText(\'${model.account}\')\n" +
        "className('android.widget.EditText').findOnce(1).setText(\'${model.password}\')\n" +
        "click('下一步')"

    private fun caoScript(): String =
        "if (!requestScreenCapture()) {\n" +
            "  toastLog('没有授予 Hamibot 屏幕截图权限');\n" +
            "  hamibot.exit();\n" +
            "}\n" +
            "sleep(1000);\n" +
            "log('开始截屏');\n" +
            "const img = captureScreen();\n" +
            "toastLog('开始识别');\n" +
            "const res = ocr.ocrImage2Native(img);\n" +
            "log(res);\n" +
            "toastLog('识别完成')"

    private val runLAScript: (model: AccountModel) -> Unit = { JSEngineHelper.runScriptStr(laScript(it)) }

    private val runCAOScript: () -> Unit = { JSEngineHelper.runScriptStr(caoScript()) }

    private var account_rv: RecyclerView? = null

    companion object {
        private const val TAG = "FloatBtnOptionViewTAG"

        fun createWindowLayoutParams(): WindowManager.LayoutParams {
            val layoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                FloatyWindowManger.getWindowType(), 520, -3)
            layoutParams.gravity = Gravity.LEFT or Gravity.TOP
            return layoutParams
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.float_btn_optn_view, this, true)?.run {
            findViewById<TextView>(R.id.tv_wifi_ip)?.text = AndServerManager.getLocalIpAddress()

            account_rv = findViewById<RecyclerView>(R.id.account_rv).also {
                it.layoutManager = GridLayoutManager(context, 3)

                GlobalScope.launch(Dispatchers.Main) {
                    val res = RequestUtils.getLofterAccounts()
                    Log.d(TAG, "res:$res")
                    try {
                        val obj = JSONObject(res)
                        val accounts = obj.optJSONArray("a")
                        val passwords = obj.optJSONArray("p")
                        it.adapter = AccountAdapter(ArrayList<AccountModel>().also { list ->
                            for (i in 0 until accounts.length()) {
                                list.add(AccountModel(accounts.optString(i), passwords.optString(i)))
                            }
                        }, runLAScript)
                    } catch (e: Exception) {
                        Log.e(TAG, "getLofterAccounts res exception: $e")
                    }
                }
            }
            findViewById<View>(R.id.capture_ocr)?.setOnClickListener { runCAOScript() }
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility != View.VISIBLE) {
            return
        }
        GlobalScope.launch(Dispatchers.Main) {
            val res = RequestUtils.getLofterAccounts()
            Log.d(TAG, "res:$res")
            val obj = JSONObject(res)
            val accounts = obj.optJSONArray("a")
            val passwords = obj.optJSONArray("p")
            (account_rv?.adapter as? AccountAdapter)?.setNewData(ArrayList<AccountModel>().also { list ->
                for (i in 0 until accounts.length()) {
                    list.add(AccountModel(accounts.optString(i), passwords.optString(i)))
                }
            })
        }
    }
}