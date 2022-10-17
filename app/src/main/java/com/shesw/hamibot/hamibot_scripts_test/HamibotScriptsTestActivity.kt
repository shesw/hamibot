package com.shesw.hamibot.hamibot_scripts_test

import com.shesw.hamibot.CslBaseActivity

class HamibotScriptsTestActivity : CslBaseActivity() {
    override fun pageTitle(): String {
        return "测试"
    }
    override fun scriptsName(): String {
        return "hamibot_script.js"
    }
}