package com.shesw.hamibot.utils

import android.content.Intent
import com.hamibot.hamibot.external.ScriptIntents
import com.stardust.app.GlobalAppContext

object JSEngineHelper {

    @JvmStatic
    fun runScriptStr(scriptStr: String) {
        val intent = Intent()
        intent.putExtra(ScriptIntents.EXTRA_KEY_PRE_EXECUTE_SCRIPT, scriptStr)
        intent.putExtra(ScriptIntents.EXTRA_KEY_LOOP_TIMES, 1)
        intent.putExtra(ScriptIntents.EXTRA_KEY_DELAY, 0)
        intent.putExtra(ScriptIntents.EXTRA_KEY_LOOP_INTERVAL, 0)
        ScriptIntents.handleIntent(GlobalAppContext.get(), intent)
    }

}